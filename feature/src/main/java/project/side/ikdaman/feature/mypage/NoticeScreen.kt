package com.example.app.ui

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.ui.AppText
import project.side.ikdaman.domain.model.Notice
import project.side.ikdaman.domain.model.NoticeDetail
import project.side.ikdaman.feature.mypage.NoticeViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun NoticeScreen(
    onBack: () -> Unit
) {
    val viewModel = hiltViewModel<NoticeViewModel>()
    val noticeUiState by viewModel.noticeUiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.noticeErrorEvent.collect {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(noticeUiState.currentPage) {
        viewModel.getNotices(noticeUiState.currentPage)
    }

    NoticeScreenUi(
        notices = noticeUiState.notices,
        currentPage = noticeUiState.currentPage,
        totalPages = noticeUiState.totalPage,
        onBack = onBack,
        onPageChange = { viewModel.changeCurrentPage(it) },
        expandedNotices = noticeUiState.expandedNotices,
        onToggleNoticeExpansion = { viewModel.toggleNoticeExpansion(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeScreenUi(
    notices: List<Notice>,
    currentPage: Int,
    totalPages: Int,
    onBack: () -> Unit,
    onPageChange: (Int) -> Unit,
    expandedNotices: Map<Long, NoticeDetail>,
    onToggleNoticeExpansion: (Long) -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    Image(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = "back",
                        modifier = Modifier
                            .padding(12.dp)
                            .size(26.dp)
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            AppText(
                text = "공지사항",
                style = TextStyle(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(horizontal = 23.dp, vertical = 20.dp)
            )
            HorizontalDivider(color = Color.Black.copy(alpha = 0.1f))
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(notices) { notice ->
                    NoticeItem(
                        notice = notice,
                        noticeDetail = expandedNotices[notice.noticeId],
                        isExpanded = expandedNotices.containsKey(notice.noticeId),
                        onClick = { onToggleNoticeExpansion(notice.noticeId) }
                    )
                    HorizontalDivider(color = Color.Black.copy(alpha = 0.1f))
                }
            }

            Pagination(
                currentPage = currentPage,
                totalPages = totalPages,
                onPageChange = onPageChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun NoticeItem(
    notice: Notice,
    noticeDetail: NoticeDetail?,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val inputTimeFormatter = remember { DateTimeFormatter.ISO_LOCAL_DATE_TIME }
    val outputTimeFormatter = remember { DateTimeFormatter.ofPattern("yyyy.MM.dd") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .animateContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 23.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                AppText(
                    text = LocalDateTime
                        .parse(notice.uploadedAt, inputTimeFormatter)
                        .format(outputTimeFormatter),
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                AppText(
                    text = notice.title,
                    style = TextStyle(fontSize = 15.sp)
                )
            }
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp
                else Icons.Default.KeyboardArrowDown,
                contentDescription = if (isExpanded) "접기" else "펼치기",
                modifier = Modifier
                    .size(24.dp)
            )
        }

        if (isExpanded && noticeDetail != null) {
            Column(Modifier.background(Color(0xFFF8F8F8))) {
                HorizontalDivider(color = Color.Black.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(8.dp))
                AppText(
                    text = noticeDetail.content,
                    style = TextStyle(fontSize = 13.sp, color = Color(0xFF444444), lineHeight = 22.sp),
                    modifier = Modifier.padding(vertical = 15.dp, horizontal = 23.dp),
                    overflow = TextOverflow.Visible,
                    maxLines = Int.MAX_VALUE
                )
            }
        }
    }
}

@Composable
private fun Pagination(
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = { if (currentPage > 1) onPageChange(currentPage - 1) },
            enabled = currentPage > 1
        ) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "이전 페이지"
            )
        }

        for (page in 1..totalPages) {
            AppText(
                text = page.toString(),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (page == currentPage) Color.Black else Color.Black.copy(alpha = 0.3f)
                ),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable(onClick = { onPageChange(page) })
            )
        }

        IconButton(
            onClick = { if (currentPage < totalPages) onPageChange(currentPage + 1) },
            enabled = currentPage < totalPages
        ) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "다음 페이지"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoticeScreenUiPreview() {
    val sampleNotices = List(8) { index ->
        Notice(
            noticeId = index.toLong(),
            title = "공지사항 제목 $index",
            uploadedAt = "2025-07-05T23:32:37.048128"
        )
    }
    val sampleNoticeDetails = List(8) { index ->
        NoticeDetail(
            noticeId = index.toLong(),
            title = "공지사항 제목 $index",
            content = "공지사항 내용 $index",
            uploadedAt = "2025-07-05T23:32:37.048128",
            noticeWriter = "관리자"
        )
    }
    NoticeScreenUi(
        notices = sampleNotices,
        currentPage = 1,
        totalPages = 3,
        onBack = {},
        onPageChange = {},
        expandedNotices = mapOf(0L to sampleNoticeDetails[0], 4L to sampleNoticeDetails[4]),
        onToggleNoticeExpansion = {}
    )
}