package project.side.ikdaman.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.ui.AppText
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.core.ui.circleRed
import project.side.ikdaman.core.view.BookProgressBar
import project.side.ikdaman.core.view.GradientBox
import project.side.ikdaman.domain.model.HomeBookItem

@Composable
fun ListBooks(
    pinnedItems: List<HomeBookItem> = emptyList(),
    unpinnedItems: List<HomeBookItem> = emptyList(),
    deleteMode: MutableState<Boolean> = remember { mutableStateOf(true) },
    onDeleteClick: (HomeBookItem) -> Unit = {},
    onPinItem: (String) -> Unit = {}
) {
    Spacer(Modifier.height(20.dp))
    SimpleBubble()
    Spacer(Modifier.height(15.dp))


    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val items = pinnedItems + unpinnedItems

    val visibleMap = remember { mutableStateMapOf<String, Boolean>() }

    // 🛠️ 리스트가 바뀌면 자동으로 visible 상태도 초기화
    LaunchedEffect(pinnedItems, unpinnedItems) {
        items.forEach { item ->
            if (visibleMap[item.id] != true) {
                visibleMap[item.id] = false
                delay(50) // 살짝 딜레이 주고
                visibleMap[item.id] = true // 등장 애니메이션 트리거
            }
        }
    }

    LazyColumn(
        state = listState,
    ) {
        itemsIndexed(
            items = items,
            key = { _, item -> item.id }
        ) { index, item ->
            val isVisible = visibleMap[item.id] ?: true

            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    animationSpec = tween(durationMillis = 200)
                ) { fullHeight -> fullHeight },
            ) {
                Box {
                    ListBooksDetail(
                        item = item,
                        isPinned = index < pinnedItems.size,
                        deleteMode = deleteMode.value,
                        onPinItem = {
                            visibleMap[item.id] = false
                            coroutineScope.launch {
                                delay(300)
                                onPinItem(item.id)
                                delay(300)
                                listState.animateScrollToItem(0)
                            }
                        },
                    )

                    if (deleteMode.value) {
                        Box(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .clip(CircleShape)
                                .background(circleRed)
                                .size(32.dp)
                                .align(Alignment.TopStart)
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
                }
            }
            if (index != pinnedItems.size + unpinnedItems.size - 1) {
                if (deleteMode.value) {
                    Spacer(Modifier.height(5.dp))
                } else {
                    Spacer(Modifier.height(10.dp))
                }
            } else {
                Spacer(Modifier.height((101 + 56).dp))
            }
        }
    }
}

@Composable
private fun AnimatedVisibilityScope.ListBooksDetail(
    item: HomeBookItem,
    isPinned: Boolean,
    deleteMode: Boolean,
    onPinItem: () -> Unit,
) {
    val clipColor = if (isPinned) Color(0xFF222221) else Color(0xFFCECECE)

    Row(
        modifier = Modifier
            .animateEnterExit(
                enter = slideInVertically(
                    animationSpec = tween(durationMillis = 300)
                ) { fullHeight -> fullHeight },
            )
            .padding(start = 20.dp, end = 20.dp, top = if (deleteMode) 5.dp else 0.dp)
            .clip(RoundedCornerShape(5.dp))
            .fillMaxWidth()
            .height(105.dp)
            .background(Color.White.copy(alpha = 0.7f))
            .padding(11.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isLoading = remember { mutableStateOf(true) }
        Box(
            Modifier
                .background(Color(0xFFF2F2F2))
                .width(58.dp)
                .height(83.dp)
        ) {
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
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
            if (isLoading.value) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(29.dp)
                        .align(Alignment.Center),
                    color = Color.Black,
                    strokeWidth = 2.dp
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    Modifier
                        .height(42.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    AppText(
                        item.title,
                        style = HomeTextStyles.bookTitleText
                    )
                    AppText(
                        item.author,
                        style = HomeTextStyles.bookAuthorText
                    )
                }
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.clip_icon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(clipColor),
                    modifier = Modifier.clickable {
                        onPinItem()
                    }
                )
            }
            Spacer(Modifier.height(26.dp))
            BookProgressBar(260, item.progress)
        }
    }
}


@Composable
private fun SimpleBubble() {
    Box {
        Column(Modifier.align(Alignment.TopCenter)) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.White)
                    .padding(vertical = 7.dp, horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(R.drawable.book_small),
                    contentDescription = null
                )
                Spacer(Modifier.width(5.dp))
                AppText("내가 읽다만 책이에요", style = HomeTextStyles.bubbleTextRegular)
            }
            Spacer(Modifier.height(5.dp))
        }
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.reverse_triangle),
            contentDescription = null,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ListBooksPreview() {
    AppTheme {
        GradientBox(
            Modifier.fillMaxSize(),
            gradient = Brush.verticalGradient(
                colors = listOf(
                    Palette.second,
                    Palette.third.copy(alpha = 0.2f),
                )
            )
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val pinnedItems = listOf(
                    HomeBookItem(
                        id = "0",
                        imageUrl = "https://picsum.photos/250/284?random=1",
                        lastEditedDateTime = System.currentTimeMillis(),
                        title = "소년이 온다1",
                        author = "한강1",
                        firstImpression = "테스트 테스트"
                    ),
                    HomeBookItem(
                        id = "1",
                        imageUrl = "https://picsum.photos/250/284?random=2",
                        lastEditedDateTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000),
                        title = "소년이 온다2",
                        author = "한강2",
                        firstImpression = "네가 죽은 뒤 장례식을 치르지 못해, 내 삶이 장례식이 되었다.\n" +
                                "네가 방수 모포에 싸여 청소차에 실려간 뒤에.\n" +
                                "용서할 수 없는 물줄기가 번쩍이며 분수대에서 뿜어져나온 뒤에.",
                        progress = 1f
                    ),
                    HomeBookItem(
                        id = "2",
                        imageUrl = "https://picsum.photos/250/284?random=3",
                        lastEditedDateTime = System.currentTimeMillis() - (48 * 60 * 60 * 1000),
                        title = "소년이 온다3",
                        author = "한강1",
                        progress = 0.5f
                    ),
                    HomeBookItem(
                        id = "3",
                        imageUrl = "https://picsum.photos/250/284?random=4",
                        lastEditedDateTime = System.currentTimeMillis() - (72 * 60 * 60 * 1000),
                        title = "소년이 온다4",
                        author = "한강1",
                        progress = 0.7f
                    ),
                )
                ListBooks(
                    pinnedItems = pinnedItems,
                )
            }
        }
    }
}