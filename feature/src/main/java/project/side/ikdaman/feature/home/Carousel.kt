package project.side.ikdaman.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import project.side.ikdaman.app.feature.R
import kotlin.math.absoluteValue

@Composable
fun BookCarousel(imageUrls: List<String> = emptyList()) {
    Box {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Carousel
            val pagerState = rememberPagerState(pageCount = { imageUrls.size })
            val coroutineScope = rememberCoroutineScope()

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(284.dp),
                pageSpacing = 32.dp, // 페이지 간 간격 유지
                contentPadding = PaddingValues(horizontal = 100.dp) // 좌우 패딩을 넓게 설정해 좌우 페이지가 벽에 걸치도록
            ) { page ->
                // 페이지 오프셋 계산
                val pageOffset =
                    (pagerState.currentPage - page + pagerState.currentPageOffsetFraction).absoluteValue
                val scale = 1f - (pageOffset * 0.2f) // 가운데 페이지가 더 크게 보이도록 스케일 조정

                CarouselItem(
                    imageUrl = imageUrls[page],
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .clickable {
                            // 탭 시 해당 페이지로 스크롤
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page)
                            }
                        }
                )
            }
        }
    }
}

@Composable
fun CarouselItem(imageUrl: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize().background(Color(0xFFF2F2F2))
    ) {
        val isLoading = remember { mutableStateOf(true) }
        Image(
            painter = rememberAsyncImagePainter(
                model = imageUrl,
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
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
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
fun CarouselPreview() {
    MaterialTheme {
        // 샘플 이미지 URL 리스트
        val imageUrls = listOf(
            "https://picsum.photos/199/284?random=1",
            "https://picsum.photos/199/284?random=2",
            "https://picsum.photos/199/284?random=3",
            "https://picsum.photos/199/284?random=4"
        )
        BookCarousel(imageUrls)
    }
}