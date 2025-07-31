package com.example.app.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.side.ikdaman.core.ui.AppText

data class Notice(
    val id: String,
    val date: String,
    val title: String,
    val content: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeScreen(
    notices: List<Notice>,
    currentPage: Int,
    totalPages: Int,
    onBack: () -> Unit,
    onPageChange: (Int) -> Unit
) {
    var expandedId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "뒤로가기",
                            modifier = Modifier.size(26.dp)
                        )
                    }
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
                itemsIndexed(notices) { _, notice ->
                    NoticeItem(
                        notice = notice,
                        isExpanded = expandedId == notice.id,
                        onClick = {
                            expandedId = if (expandedId == notice.id) null else notice.id
                        }
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
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val rotationDegree by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
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
                    text = notice.date,
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
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = if (isExpanded) "접기" else "펼치기",
                modifier = Modifier
                    .size(24.dp)
                    .rotate(rotationDegree)
            )
        }

        if (isExpanded) {
            Column(Modifier.background(Color(0xFFF8F8F8))) {
                HorizontalDivider(color = Color.Black.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(8.dp))
                AppText(
                    text = notice.content,
                    style = TextStyle(fontSize = 13.sp, color = Color(0xFF444444)),
                    modifier = Modifier.padding(vertical = 15.dp, horizontal = 23.dp)
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
fun NoticeScreenPreview() {
    val sampleNotices = List(8) { index ->
        Notice(
            id = index.toString(),
            date = "25.05.31",
            title = "공지사항 제목 #$index",
            content = "공지사항 상세 내용"
        )
    }
    NoticeScreen(
        notices = sampleNotices,
        currentPage = 1,
        totalPages = 3,
        onBack = {},
        onPageChange = {}
    )
}