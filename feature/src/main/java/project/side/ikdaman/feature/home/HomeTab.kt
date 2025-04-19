package project.side.ikdaman.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.navigation.MAIN_ROUTE
import project.side.ikdaman.core.ui.AppText
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.core.view.BookProgressBar

@Composable
fun HomeTab(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(
        navController.getBackStackEntry(MAIN_ROUTE)
    )
) {
    @Suppress("UNUSED_VARIABLE")
    val count = viewModel.count.collectAsState()
    HomeTabUI(
        onNavigateTo = {
            navController.navigate(it)
        }
    )
}

enum class HomeTabViewMode {
    CAROUSEL,
    LIST
}

@Composable
fun HomeTabUI(
    paletteViewState: MutableState<Boolean> = remember { mutableStateOf(false) },
    onNavigateTo: (String) -> Unit = {}
) {
    val paletteColors = listOf(
        Palette.first,
        Palette.second,
        Palette.third,
        Palette.fourth,
        Palette.fifth
    )

    val selectedColor = remember { mutableStateOf(Palette.first) }
    val selectedViewMode = remember { mutableStateOf(HomeTabViewMode.CAROUSEL) }

    GradientBox(
        Modifier.fillMaxSize(),
        gradient = Brush.verticalGradient(
            colors = listOf(
                selectedColor.value,
                selectedColor.value.copy(alpha = 0.2f),
            )
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 6.dp, end = 18.dp, top = 6.dp)
                    .fillMaxWidth()
            ) {
                Surface(
                    shadowElevation = 4.dp,
                    shape = CircleShape,
                    modifier = Modifier.padding(12.dp).clickable {
                        paletteViewState.value = !paletteViewState.value
                    }
                ) {
                    Box(
                        Modifier
                            .border(width = 1.5.dp, color = Color.White, shape = CircleShape)
                            .background(selectedColor.value)
                            .size(23.dp)
                    )
                }
                Spacer(Modifier.width(100.dp))
                if (selectedViewMode.value == HomeTabViewMode.CAROUSEL) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.list),
                        contentDescription = null
                    )
                } else {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.expand),
                        contentDescription = null
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            val day = 31
            val progress = 0.5f
            LeftDayBubble(day)
            Spacer(Modifier.height(17.dp))
            val imageUrls = listOf(
                "https://picsum.photos/199/284?random=4",
                "https://picsum.photos/199/284?random=5",
                "https://picsum.photos/199/284?random=6",
                "null"
            )
            BookCarousel(imageUrls)
            Spacer(Modifier.height(19.dp))
            Column(verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.height(42.dp)) {
                AppText("소년이 온다", style = HomeTextStyles.bookTitleText)
                AppText("한강", style = HomeTextStyles.bookAuthorText)
            }
            Spacer(Modifier.height(10.dp))
            val barWidth = LocalConfiguration.current.screenWidthDp - 40
            BookProgressBar(barWidth, progress, modifier = Modifier.padding(horizontal = 20.dp))
            Spacer(Modifier.height(30.dp))
            Column(
                Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth()
                    .height(135.dp)
                    .background(Color.White)
                    .padding(start = 25.dp, end = 25.dp, top = 25.dp)

            ) {
                AppText(
                    "\uD83D\uDC95 책의 첫인상",
                    modifier = Modifier,
                    style = HomeTextStyles.bottomTitle,
                )
                Spacer(Modifier.height(10.dp))
                AppText(
                    "네가 죽은 뒤 장례식을 치르지 못해, 내 삶이 장례식이 되었다.\n" +
                            "네가 방수 모포에 싸여 청소차에 실려간 뒤에.\n" +
                            "용서할 수 없는 물줄기가 번쩍이며 분수대에서 뿜어져나온 뒤에.",
                    style = HomeTextStyles.bottomDescription,
                    maxLines = 3
                )
            }
            Spacer(Modifier.height(13.dp))
            Box {
                Column(modifier = Modifier.padding(8.dp)) {
                    AppText("이 책의 기록 추가 +", style = HomeTextStyles.buttonText)
                    Box(
                        Modifier
                            .width(87.dp)
                            .height(1.dp)
                            .background(Color.Black)
                    )
                }
            }
        }

        if (paletteViewState.value) {
            Box(Modifier.fillMaxSize().clickable {
                paletteViewState.value = false
            })
            Box(Modifier.padding(top = 59.dp, start = 20.dp).align(Alignment.TopStart)) {
                PaletteView(paletteColors, selectedColor, paletteViewState) {
                    selectedColor.value = it
                    paletteViewState.value = false
                }
            }
        }
    }
}

@Composable
private fun PaletteView(
    paletteColors: List<Color>,
    selectedColor: MutableState<Color>,
    paletteViewState: MutableState<Boolean>,
    onSelected: (Color) -> Unit = {}
) {
    Row(Modifier.clip(RoundedCornerShape(5.dp)).background(Color.White).padding(15.dp)) {
        val selectedBorderColor = Color(0xFF565656)
        paletteColors.forEachIndexed { index, color ->
            val isSelected = selectedColor.value == color
            Surface(
                shadowElevation = 4.dp,
                shape = CircleShape,
                modifier = Modifier.clickable {
                    onSelected(color)
                }
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(color)
                        .size(23.dp)
                        .border(width = 1.5.dp, color = if (isSelected) selectedBorderColor else Color.White, shape = CircleShape)
                        .clickable {
                            selectedColor.value = color
                            paletteViewState.value = false
                        }
                )
            }
            if (index != paletteColors.size - 1) {
                Spacer(Modifier.width(10.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PalettePreview() {
    AppTheme {
        val paletteColors = listOf(
            Palette.first,
            Palette.second,
            Palette.third,
            Palette.fourth,
            Palette.fifth
        )
        val selectedColor = remember { mutableStateOf(Palette.first) }
        val paletteViewState = remember { mutableStateOf(false) }
        PaletteView(paletteColors, selectedColor, paletteViewState)
    }
}

@Composable
private fun LeftDayBubble(day: Int = 31) {
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
                AppText("${day}일 ", style = HomeTextStyles.bubbleTextBold)
                AppText("전에 읽다만 책이에요", style = HomeTextStyles.bubbleTextRegular)
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
fun GradientBox(
    modifier: Modifier = Modifier,
    gradient: Brush,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(
        modifier = modifier.background(brush = gradient),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
@Preview(showBackground = true, widthDp = 393, heightDp = 869-56-47)
fun HomeTabPreview() {
    AppTheme {
        HomeTabUI(
            paletteViewState = remember { mutableStateOf(true) }
        )
    }
}