package project.side.ikdaman.feature.search

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.distinctUntilChanged
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.navigation.ADD_BOOK_ROUTE
import project.side.ikdaman.core.navigation.BARCODE_ROUTE
import project.side.ikdaman.core.navigation.FromWhere
import project.side.ikdaman.core.navigation.HOME_ROUTE
import project.side.ikdaman.core.navigation.MAIN_ROUTE
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.core.ui.PretendardFontFamily
import project.side.ikdaman.core.utils.oneClick
import project.side.ikdaman.core.view.AddBookButton
import project.side.ikdaman.core.view.GradientBox
import project.side.ikdaman.domain.model.BookItem
import project.side.ikdaman.domain.model.BookSubInfo

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchTab(
    appNavController: NavController,
    mainNavController: NavController,
    viewModel: SearchViewModel = hiltViewModel(
        appNavController.getBackStackEntry(MAIN_ROUTE)
    )
) {
    val bookSearch by viewModel.searchResult.collectAsStateWithLifecycle()
    val searchKeyword by viewModel.searchKeyword.collectAsStateWithLifecycle()
    val selectedColor by viewModel.selectedColor.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.selectedBookIsbn.collect {
            appNavController.navigate("$ADD_BOOK_ROUTE/$it")
        }
    }

    LaunchedEffect(Unit) {
        val needToGoHome = appNavController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("navigateToHome")
        if (needToGoHome == true) {
            mainNavController.navigate(HOME_ROUTE)
        }
    }

    SearchTabUI(
        selectedColor = selectedColor,
        onSearchKeywordChange = viewModel::updateSearchKeyword,
        searchKeyword = searchKeyword,
        bookItems = bookSearch,
        onClickAddBookButton = viewModel::selectBook,
        onLoadMoreBooks = viewModel::loadMore,
        onNavigateToBarcodeScanner = {
            appNavController.navigate("${BARCODE_ROUTE}/${FromWhere.FROM_SEARCH}")
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchTabUI(
    selectedColor: Color = Palette.first,
    onSearchKeywordChange: (String) -> Unit = {},
    searchKeyword: String = "",
    bookItems: List<BookItem> = listOf(),
    onClickAddBookButton: (String) -> Unit = {},
    onLoadMoreBooks: () -> Unit = {},
    onNavigateToBarcodeScanner: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Text(
                    text = "책 제목으로 검색하기",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(vertical = 15.dp),
                    style = TextStyle(
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                )
            }
        },
    ) { paddingValues ->
        GradientBox(
            Modifier
                .fillMaxSize(),
            gradient = Brush.verticalGradient(
                colors = listOf(
                    selectedColor,
                    selectedColor.copy(alpha = 0.2f),
                )
            ),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp),
            ) {
                SearchTextField(
                    modifier = Modifier
                        .padding(vertical = 24.dp),
                    searchText = searchKeyword,
                    onSearchTextChanged = onSearchKeywordChange,
                    onNavigateToBarcodeScanner = onNavigateToBarcodeScanner
                )
                if (bookItems.isEmpty()) {
                    NoSearchResultScreen(searchKeyword)
                } else {
                    SearchResultScreen(
                        bookItems = bookItems,
                        onClickAddBookButton = onClickAddBookButton,
                        onLoadMoreBooks = onLoadMoreBooks
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchResultScreen(
    bookItems: List<BookItem>,
    onClickAddBookButton: (String) -> Unit,
    onLoadMoreBooks: () -> Unit
) {
    val listState = rememberLazyListState()
    LaunchedEffect(listState, bookItems) {
        snapshotFlow {
            val visibleItems = listState.layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) 0
            else visibleItems.last().index
        }
            .distinctUntilChanged()
            .collect { lastIndex ->
                if (lastIndex == bookItems.size - 1) {
                    onLoadMoreBooks()
                }
            }
    }
    LazyColumn(
        state = listState,
        modifier = Modifier.padding(bottom = 56.dp)
    ) {
        items(bookItems) { item ->
            SearchResultItem(
                bookItem = item,
                onClickAddBookButton = onClickAddBookButton
            )
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.5f))
            )
        }
    }
}

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onNavigateToBarcodeScanner: () -> Unit = {}
) {
    BasicTextField(
        modifier = modifier
            .heightIn(min = 48.dp)
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(5.dp),
                color = Color.White
            ),
        value = searchText,
        onValueChange = {
            onSearchTextChanged(it)
        },
        textStyle = TextStyle(
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
        ),
        maxLines = 1,
        decorationBox = { innerTextField ->
            Box(modifier = Modifier) {
                if (searchText.isEmpty()) {
                    Text(
                        "책 제목을 검색해주세요.",
                        color = Color(0xff989898),
                        style = TextStyle(
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 15.sp
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(horizontal = 15.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 15.dp, end = 80.dp)
                ) {
                    innerTextField()
                }
                Row(Modifier
                    .padding(end = 15.dp)
                    .align(Alignment.CenterEnd)
                    .oneClick {
                        onNavigateToBarcodeScanner()
                    }) {
                    Icon(
                        painter = painterResource(R.drawable.camera_scan),
                        contentDescription = "Camera",
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                            .size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(R.drawable.magnifier),
                        contentDescription = "Search",
                        Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                            .size(24.dp)
                    )
                }
            }
        }
    )
}

@Composable
private fun SearchResultItem(
    bookItem: BookItem,
    onClickAddBookButton: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp)
    ) {
        Row {
            AsyncImage(
                model = bookItem.cover,
                contentDescription = "Book Cover",
                modifier = Modifier
                    .size(width = 80.dp, height = 114.dp)
            )
            Column(modifier = Modifier.padding(start = 18.dp)) {
                Text(
                    text = bookItem.title,
                    style = TextStyle(
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    ),
                    modifier = Modifier.padding(bottom = 3.dp)
                )
                Text(
                    text = bookItem.author,
                    style = TextStyle(
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    )
                )
            }
        }
        AddBookButton(
            onClick = { onClickAddBookButton(bookItem.isbn) },
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

@Composable
private fun NoSearchResultScreen(searchKeyword: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 174.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (searchKeyword.isNotEmpty()) {
            Text(
                text = searchKeyword,
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )
            Text(
                text = "에 대한 검색결과가 없어요.\n\n검색 결과를 다시한번 확인해 주세요.",
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun SearchTabUIPreview() {
    AppTheme {
        SearchTabUI(
            searchKeyword = "소년이 온다 온다 온다 온다 온다 온다 온다 온다 온다",
            bookItems = List(5) {
                BookItem(
                    title = "소년이 온다(개정판)",
                    author = "한강",
                    cover = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/4808936434120.jpg",
                    isbn = "",
                    publisher = "창비",
                    subInfo = BookSubInfo("279"),
                    itemId = 0,
                    link = ""
                )
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SearchTabUIPreview_No_Result() {
    AppTheme {
        SearchTabUI(
            searchKeyword = "소년ㅇㄴaasdfasdfasdfasdfasdfㅇ",
            bookItems = emptyList(),
        )
    }
}
