package project.side.ikdaman.feature.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.ui.AppText
import project.side.ikdaman.core.ui.circleRed
import project.side.ikdaman.domain.model.HomeBookItem
import kotlin.math.absoluteValue

@Composable
fun BookCarousel(
    deleteMode: MutableState<Boolean> = remember { mutableStateOf(false) },
    selectedBookIndex: MutableState<Int> = mutableStateOf(0),
    items: List<HomeBookItem> = emptyList(),
    onDeleteClick: (HomeBookItem) -> Unit = {},
    onBookClicked: (String) -> Unit = {},
    scrollToTop: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        scrollToTop()
    }

    // Carousel
    val pagerState = rememberPagerState(pageCount = { items.size }, initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    // 정확히 가운데로 오기 위해 화면 가로 길이에서 책 너비 빼기
    val carouselPadding = calculateHorizontalPadding()

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth(),
        pageSpacing = 32.dp, // 페이지 간 간격 유지
        contentPadding = PaddingValues(horizontal = carouselPadding.dp) // 좌우 패딩을 넓게 설정해 좌우 페이지가 벽에 걸치도록
    ) { page ->
        selectedBookIndex.value = pagerState.currentPage

        // 페이지 오프셋 계산
        val pageOffset =
            (pagerState.currentPage - page + pagerState.currentPageOffsetFraction).absoluteValue
        val scale = 1f - (pageOffset * 0.2f) // 가운데 페이지가 더 크게 보이도록 스케일 조정

        CarouselItemView(
            item = items[page],
            isNotSelectedPage = page != pagerState.currentPage,
            deleteMode = deleteMode,
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }.pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            coroutineScope.launch {
                                if (!deleteMode.value && page == pagerState.currentPage) {
                                    onBookClicked(items[page].id)
                                }
                                pagerState.animateScrollToPage(page)
                            }
                        },
                        onLongPress = {
                            deleteMode.value = true
                        }
                    )
                },
            onDeleteClick = onDeleteClick,
        )
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun calculateHorizontalPadding(): Int {
    return LocalConfiguration.current.screenWidthDp / 2 - 199 / 2
}

@Composable
fun CarouselItemView(
    item: HomeBookItem,
    isNotSelectedPage: Boolean = false,
    deleteMode: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    onDeleteClick: (HomeBookItem) -> Unit = {}
) {

    val circleTextStyle = TextStyle(
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        letterSpacing = (-0.4).sp
    )

    Box(
        modifier = modifier
            .width(199.dp)
            .height(284.dp)
            .background(Color(0xFFF2F2F2))
    ) {
        val isLoading = remember { mutableStateOf(true) }
        Image(
            painter = rememberAsyncImagePainter(
                model = item.imageUrl,
                placeholder = null,
                onLoading = {
                    isLoading.value = true
                },
                onSuccess = {
                    isLoading.value = false
                },
                onError = {
                    isLoading.value = false
                },
                error = painterResource(R.drawable.no_image)
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().graphicsLayer {
                alpha = if (deleteMode.value && isNotSelectedPage) {
                    0.3f
                } else {
                    1f
                }
            },
            contentScale = ContentScale.Fit
        )
        // item.addedDateTime (Long Type) 값과 현재 시간을 비교해서 24시간 이내인지 확인
        val isNew = (System.currentTimeMillis() - item.lastEditedDateTime) < 24 * 60 * 60 * 1000
        if (isNew) {
            Box(
                modifier = Modifier
                    .padding(top = 9.dp, start = 9.dp)
                    .clip(CircleShape)
                    .background(circleRed)
                    .size(32.dp)
            ) {
                AppText(
                    "N",
                    style = circleTextStyle,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
        if (deleteMode.value) {
            Box(
                modifier = Modifier
                    .padding(top = 9.dp, end = 9.dp)
                    .clip(CircleShape)
                    .background(circleRed)
                    .size(32.dp)
                    .align(Alignment.TopEnd)
                    .clickable {
                        onDeleteClick(item)
                    }
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .width(14.dp)
                        .height(2.dp)
                        .background(Color.White)
                        .align(Alignment.Center)
                )
            }
        }

        if (isLoading.value) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(50.dp),
                color = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarouselItemPreView() {
    MaterialTheme {
        Row {
            CarouselItemView(
                item = HomeBookItem(
                    id = "0",
                    imageUrl = "https://picsum.photos/250/284?random=1",
                    lastEditedDateTime = System.currentTimeMillis(),
                    title = "소년이 온다",
                    author = "한강"
                ),
                deleteMode = remember { mutableStateOf(true) },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarouselPreview() {
    MaterialTheme {
        // 샘플 이미지 URL 리스트
        val items = listOf(
            HomeBookItem(
                id = "0",
                imageUrl = "https://picsum.photos/250/284?random=1",
                lastEditedDateTime = System.currentTimeMillis(),
                title = "소년이 온다1",
                author = "한강1"
            ),
            HomeBookItem(
                id = "1",
                imageUrl = "https://picsum.photos/250/284?random=2",
                lastEditedDateTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000),
                title = "소년이 온다1",
                author = "한강1"
            ),
            HomeBookItem(
                id = "2",
                imageUrl = "https://picsum.photos/250/284?random=3",
                lastEditedDateTime = System.currentTimeMillis() - (48 * 60 * 60 * 1000),
                title = "소년이 온다1",
                author = "한강1"
            ),
            HomeBookItem(
                id = "3",
                imageUrl = "https://picsum.photos/250/284?random=4",
                lastEditedDateTime = System.currentTimeMillis() - (72 * 60 * 60 * 1000),
                title = "소년이 온다1",
                author = "한강1"
            ),
        )
        BookCarousel(items = items, selectedBookIndex = remember { mutableStateOf(0) })
    }
}