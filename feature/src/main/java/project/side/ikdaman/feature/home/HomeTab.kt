package project.side.ikdaman.feature.home

import ExpandableInlineText
import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.navigation.ADD_BOOK_RECORD
import project.side.ikdaman.core.navigation.HOME_ROUTE
import project.side.ikdaman.core.navigation.MAIN_ROUTE
import project.side.ikdaman.core.ui.AppText
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.core.utils.oneClick
import project.side.ikdaman.core.view.BookProgressBarWithText
import project.side.ikdaman.core.view.DeleteDialog
import project.side.ikdaman.core.view.GradientBox
import project.side.ikdaman.domain.model.HomeBookItem
import project.side.ikdaman.feature.add_notes.RecordType

@Composable
fun HomeTab(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(
        navController.getBackStackEntry(MAIN_ROUTE)
    )
) {
    val deleteDialogState = remember { MutableTransitionState(false) }
    val deleteItem = remember { mutableStateOf<HomeBookItem?>(null) }
    val selectedColor = viewModel.selectedColor.collectAsState().value

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect {
            if (it.destination.route == HOME_ROUTE) {
                viewModel.getBooks()
            }
        }
    }

    HomeTabUI(
        selectedColor = selectedColor,
        books = viewModel.books.collectAsState().value,
        pinnedItems = viewModel.pinnedItems.collectAsState().value,
        unpinnedItems = viewModel.unpinnedItems.collectAsState().value,
        onPinItem = {
            viewModel.pinItem(it)
        },
        onDeleteClick = {
            deleteItem.value = it
            deleteDialogState.targetState = true
        },
        onSelectColor = {
            viewModel.saveSelectedColor(it)
        },
        onAddRecord = { bookId ->
            navController.navigate("$ADD_BOOK_RECORD/${RecordType.MIDDLE.name}/$bookId")
        },
        onBookClicked = {
            navController.navigate("")
        }
    )

    DeleteDialog(
        dialogState = deleteDialogState,
        onDelete = {
            viewModel.deleteItem(deleteItem.value!!)
            deleteDialogState.targetState = false
        }
    )
}

enum class HomeTabViewMode {
    CAROUSEL,
    LIST
}

@Composable
fun HomeTabUI(
    selectedColor: Color = Palette.first,
    books: List<HomeBookItem> = listOf(),
    pinnedItems: List<HomeBookItem> = emptyList(),
    unpinnedItems: List<HomeBookItem> = emptyList(),
    selectedViewMode: MutableState<HomeTabViewMode> = remember { mutableStateOf(HomeTabViewMode.CAROUSEL) },
    paletteViewState: MutableState<Boolean> = remember { mutableStateOf(false) },
    onPinItem: (String) -> Unit = {},
    onDeleteClick: (HomeBookItem) -> Unit = {},
    onSelectColor: (Color) -> Unit = {},
    onAddRecord: (String) -> Unit = {},
    onBookClicked: (String) -> Unit = {}
) {
    val selectedBookIndex = remember { mutableStateOf(0) }
    val deleteMode = remember { mutableStateOf(false) }

    GradientBox(
        Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                deleteMode.value = false
            },
        gradient = Brush.verticalGradient(
            colors = listOf(
                selectedColor,
                selectedColor.copy(alpha = 0.2f),
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
                ColorPaletteButton(paletteViewState, selectedColor)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!deleteMode.value && selectedViewMode.value == HomeTabViewMode.CAROUSEL) {
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.bin),
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                deleteMode.value = !deleteMode.value
                            }
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    if (selectedViewMode.value == HomeTabViewMode.CAROUSEL) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.list),
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                selectedViewMode.value = HomeTabViewMode.LIST
                            }
                        )
                    } else {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.expand),
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                selectedViewMode.value = HomeTabViewMode.CAROUSEL
                            }
                        )
                    }
                }
            }

            if (books.isNotEmpty()) {
                if (selectedViewMode.value == HomeTabViewMode.CAROUSEL) {
                    CarouselBooks(deleteMode, selectedBookIndex, books, onDeleteClick, onAddRecord, onBookClicked)
                } else {
                    ListBooks(
                        pinnedItems = pinnedItems,
                        unpinnedItems = unpinnedItems,
                        onPinItem = onPinItem
                    )
                }
            } else {
                EmptyBookView()
            }
        }

        if (paletteViewState.value) {
            Box(
                Modifier
                    .fillMaxSize()
                    .clickable {
                        paletteViewState.value = false
                    })
            Box(
                Modifier
                    .padding(top = 59.dp, start = 20.dp)
                    .align(Alignment.TopStart)
            ) {
                PaletteView(Palette.paletteColors, selectedColor, paletteViewState) {
                    onSelectColor(it)
                    paletteViewState.value = false
                }
            }
        }
    }
}


@Composable
private fun ColorPaletteButton(
    paletteViewState: MutableState<Boolean>,
    selectedColor: Color
) {
    Surface(
        shadowElevation = 4.dp,
        shape = CircleShape,
        modifier = Modifier
            .padding(12.dp)
            .clickable {
                paletteViewState.value = !paletteViewState.value
            }
    ) {
        Box(
            Modifier
                .border(width = 1.5.dp, color = Color.White, shape = CircleShape)
                .background(selectedColor)
                .size(23.dp)
        )
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun CarouselBooks(
    deleteMode: MutableState<Boolean>,
    selectedBookIndex: MutableState<Int>,
    books: List<HomeBookItem>,
    onDeleteClick: (HomeBookItem) -> Unit = {},
    onAddRecord: (String) -> Unit = {},
    onBookClicked: (String) -> Unit = {}
) {
    val state = rememberScrollState()
    Column(Modifier.verticalScroll(state, reverseScrolling = true), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(20.dp))
        LeftDayBubble(books[selectedBookIndex.value])
        Spacer(Modifier.height(17.dp))
        BookCarousel(
            deleteMode = deleteMode,
            selectedBookIndex = selectedBookIndex,
            items = books,
            onDeleteClick = onDeleteClick,
            onBookClicked = onBookClicked
        )
        Spacer(Modifier.height(19.dp))
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.height(42.dp)
        ) {
            AppText(
                books[selectedBookIndex.value].title,
                style = HomeTextStyles.bookTitleText
            )
            AppText(
                books[selectedBookIndex.value].author,
                style = HomeTextStyles.bookAuthorText
            )
        }
        Spacer(Modifier.height(10.dp))
        BookProgressBarWithText(
            LocalConfiguration.current.screenWidthDp - 40,
            books[selectedBookIndex.value].progress,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(Modifier.height(20.dp))
        Box(
            Modifier.oneClick(500) {
                onAddRecord(books[selectedBookIndex.value].id)
            }
        ) {
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
        Spacer(Modifier.height(30.dp))
        val isExpanded = remember { mutableStateOf(false) }
        val isImpressionEmpty = books[selectedBookIndex.value].firstImpression.isEmpty()
        if (isImpressionEmpty) {
            Column(
                Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 25.dp, horizontal = 20.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppText(
                        "\uD83D\uDC95 책의 첫인상",
                        style = HomeTextStyles.bottomTitle,
                    )
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.pencil),
                        contentDescription = null,
                        Modifier.size(24.dp)
                    )
                }
                Spacer(Modifier.height(10.dp))
                AppText(
                    firstImpressionText(books, selectedBookIndex),
                    style = HomeTextStyles.bottomDescription.copy(
                        color = Color(0xFF333333)
                    ),
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        } else {
            Column(
                Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth()
                    .animateContentSize(
                        animationSpec = tween(300)
                    )
                    .background(Color.White)
                    .padding(25.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppText(
                        "\uD83D\uDC95 책의 첫인상",
                        style = HomeTextStyles.bottomTitle,
                    )
                    if (isExpanded.value) {
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.arrow_small_up),
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                isExpanded.value = false
                            }
                        )
                    } else {
                        Box(Modifier.size(24.dp))
                    }
                }
                Spacer(Modifier.height(10.dp))
                ExpandableInlineText(
                    text = firstImpressionText(books, selectedBookIndex),
                    isExpanded = isExpanded,
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(
                            animationSpec = tween(100)
                        ),
                )
            }
        }
        Spacer(Modifier.height(111.dp))
    }
}

@Composable
private fun EmptyBookView() {
    Spacer(Modifier.height(35.dp))
    Box(
        Modifier
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .height(175.dp)
            .background(Color.White.copy(alpha = 0.6f))
    ) {
        Text(
            "+\n" +
                    "가지고 있는 책이 없어요.\n" +
                    "독서를 추가해보세요 \uD83E\uDD13\uFE0F",
            textAlign = TextAlign.Center,
            style = HomeTextStyles.emptyBookText,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


private fun firstImpressionText(
    books: List<HomeBookItem>,
    selectedBookIndex: MutableState<Int>
): String {
    return books[selectedBookIndex.value].firstImpression.ifEmpty {
        "처음 책을 보고 들었던 생각을 짧게 적어보세요.\n" +
                "독서가 마음처럼 잘되지 않을 때, 나에게 힘을 줄 거예요!"
    }
}

@Composable
private fun PaletteView(
    paletteColors: List<Color>,
    selectedColor: Color,
    paletteViewState: MutableState<Boolean>,
    onSelected: (Color) -> Unit = {}
) {
    Row(
        Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(Color.White)
            .padding(15.dp)
    ) {
        val selectedBorderColor = Color(0xFF565656)
        paletteColors.forEachIndexed { index, color ->
            val isSelected = selectedColor == color
            Surface(
                shadowElevation = 4.dp,
                shape = CircleShape,
                modifier = Modifier.clickable {
                    onSelected(color)
                    paletteViewState.value = false
                }
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(color)
                        .size(23.dp)
                        .border(
                            width = 1.5.dp,
                            color = if (isSelected) selectedBorderColor else Color.White,
                            shape = CircleShape
                        )
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
        val selectedColor = Palette.second
        val paletteViewState = remember { mutableStateOf(false) }
        PaletteView(paletteColors, selectedColor, paletteViewState)
    }
}

@Composable
private fun LeftDayBubble(bookItem: HomeBookItem) {
    val day = bookItem.getElapsedDays()
    val isCompleted = bookItem.isCompleted()
    if (isCompleted) {
        Spacer(Modifier.height(41.dp))
        return
    }
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
                if (day == 0) {
                    AppText("오늘 ", style = HomeTextStyles.bubbleTextBold)
                    AppText("읽다만 책이에요", style = HomeTextStyles.bubbleTextRegular)
                } else {
                    AppText("${day}일 ", style = HomeTextStyles.bubbleTextBold)
                    AppText("전에 읽다만 책이에요", style = HomeTextStyles.bubbleTextRegular)
                }
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
@Preview(showBackground = true, widthDp = 420, heightDp = 869 - 56 - 47)
fun CarouselHomeTabPreview() {
    AppTheme {
        HomeTabUI(
            books = listOf(
                HomeBookItem(
                    id = "0",
                    imageUrl = "https://picsum.photos/250/284?random=1",
                    lastEditedDateTime = System.currentTimeMillis(),
                    title = "소년이 온다1",
                    author = "한강1",
                    firstImpression = "네가 죽은 뒤 장례식을 치르지 못해, 내 삶이 장례식이 되었다.\n" +
                            "네가 방수 모포에 싸여 청소차에 실려간 뒤에.\n" +
                            "용서할 수 없는 물줄기가 번쩍이며 분수대에서 뿜어져나온 뒤에. 기나긴 글이 이어집니다",
                    progress = 0.1f
                ),
                HomeBookItem(
                    id = "1",
                    imageUrl = "https://picsum.photos/250/284?random=2",
                    lastEditedDateTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000),
                    title = "소년이 온다2",
                    author = "한강2",
                    firstImpression = "",
                    progress = 1f
                ),
                HomeBookItem(
                    id = "2",
                    imageUrl = "https://picsum.photos/250/284?random=3",
                    lastEditedDateTime = System.currentTimeMillis() - (48 * 60 * 60 * 1000),
                    title = "소년이 온다3",
                    author = "한강1"
                ),
                HomeBookItem(
                    id = "3",
                    imageUrl = "https://picsum.photos/250/284?random=4",
                    lastEditedDateTime = System.currentTimeMillis() - (72 * 60 * 60 * 1000),
                    title = "소년이 온다4",
                    author = "한강1"
                ),
            ),
            paletteViewState = remember { mutableStateOf(false) }
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 393, heightDp = 869 - 56 - 47)
fun ListHomeTabPreview() {
    AppTheme {
        HomeTabUI(
            books = listOf(
                HomeBookItem(
                    id = "0",
                    imageUrl = "https://picsum.photos/250/284?random=1",
                    lastEditedDateTime = System.currentTimeMillis(),
                    title = "소년이 온다1",
                    author = "한강1",
                    firstImpression = ""
                ),
                HomeBookItem(
                    id = "1",
                    imageUrl = "https://picsum.photos/250/284?random=2",
                    lastEditedDateTime = System.currentTimeMillis() - (12 * 60 * 60 * 1000),
                    title = "소년이 온다2",
                    author = "한강2",
                    firstImpression = "",
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
            ),
            pinnedItems = listOf(
                HomeBookItem(
                    id = "0",
                    imageUrl = "https://picsum.photos/250/284?random=1",
                    lastEditedDateTime = System.currentTimeMillis(),
                    title = "소년이 온다1",
                    author = "한강1",
                    firstImpression = ""
                ),
                HomeBookItem(
                    id = "1",
                    imageUrl = "https://picsum.photos/250/284?random=2",
                    lastEditedDateTime = System.currentTimeMillis() - (12 * 60 * 60 * 1000),
                    title = "소년이 온다2",
                    author = "한강2",
                    firstImpression = "",
                    progress = 1f
                ),
            ),
            unpinnedItems = listOf(
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
            ),
            selectedViewMode = remember { mutableStateOf(HomeTabViewMode.LIST) },
        )
    }
}


@Composable
@Preview(showBackground = true, widthDp = 393, heightDp = 869 - 56 - 47)
fun EmptyHomeTabPreview() {
    AppTheme {
        HomeTabUI()
    }
}