package project.side.ikdaman.feature.search

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.navigation.MAIN_ROUTE
import project.side.ikdaman.core.navigation.SEARCH_INFO_ROUTE
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.core.ui.PretendardFontFamily
import project.side.ikdaman.core.view.GradientBox
import project.side.ikdaman.domain.model.BookItem
import project.side.ikdaman.domain.model.BookSearchResult
import project.side.ikdaman.domain.model.BookSubInfo

private const val TAG = "SearchScreen"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchTab(
    appNavController: NavController,
    viewModel: SearchViewModel = hiltViewModel(
        appNavController.getBackStackEntry(MAIN_ROUTE)
    )
) {
    val bookSearch by viewModel.searchResult.collectAsStateWithLifecycle()
    val searchKeyword by viewModel.searchKeyword.collectAsStateWithLifecycle()
    val selectedColor by viewModel.selectedColor.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.selectedBookIsbn.collect {
            appNavController.navigate("$SEARCH_INFO_ROUTE/$it")
        }
    }

    SearchTabUI(
        selectedColor = selectedColor,
        onBack = { appNavController.popBackStack() },
        onSearchKeywordChange = {
            viewModel.updateSearchKeyword(it)
        },
        searchKeyword = searchKeyword,
        bookSearchResult = bookSearch,
        onClickAddBookButton = { viewModel.emitSelectedBookIsbn(it) }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchTabUI(
    selectedColor: Color = Palette.first,
    onSearchKeywordChange: (String) -> Unit = {},
    searchKeyword: String = "",
    onBack: () -> Unit = {},
    bookSearchResult: BookSearchResult? = BookSearchResult(),
    onClickAddBookButton: (Int) -> Unit = {}
) {
    Scaffold(
        topBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = "Back",
                        Modifier.size(26.dp)
                    )
                }
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
            )
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SearchTextField(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(top = 24.dp),
                    searchText = searchKeyword,
                    onSearchTextChanged = onSearchKeywordChange
                )
                SearchResultScreen(
                    searchKeyword = searchKeyword,
                    bookSearchResult = bookSearchResult,
                    onClickAddBookButton = onClickAddBookButton,
                )
            }
        }
    }
}

@Composable
private fun SearchResultScreen(
    searchKeyword: String,
    bookSearchResult: BookSearchResult?,
    onClickAddBookButton: (Int) -> Unit
) {
    if (bookSearchResult == null || bookSearchResult.totalBookCount == 0) {
        NoSearchResultScreen(searchKeyword)
    } else {
        LazyColumn(modifier = Modifier.padding(top = 24.dp)) {
            items(bookSearchResult.books.withIndex().toList()) { (index, item) ->
                SearchResultItem(
                    bookItem = item,
                    index = index,
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
}

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChanged: (String) -> Unit
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
            fontSize = 15.sp
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier

            ) {
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
                        .padding(horizontal = 15.dp)
                ) {
                    innerTextField()
                }
                IconButton(onClick = {}, modifier = Modifier.align(Alignment.CenterEnd)) {
                    Icon(
                        painter = painterResource(R.drawable.magnifier),
                        contentDescription = "Search",
                        Modifier.size(24.dp)
                    )
                }
            }
        }
    )
}

@Composable
private fun SearchResultItem(
    bookItem: BookItem,
    index: Int,
    onClickAddBookButton: (Int) -> Unit
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
        SearchResultAddButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            index = index,
            onClick = onClickAddBookButton
        )
    }
}

@Composable
private fun SearchResultAddButton(
    modifier: Modifier,
    index: Int,
    onClick: (Int) -> Unit = {},
) {
    Button(
        onClick = { onClick(index) },
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
        contentPadding = PaddingValues(vertical = 5.dp, horizontal = 12.dp)
    ) {
        Text(
            text = "이 책 추가 +",
            style = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
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
            searchKeyword = "소년",
            bookSearchResult = BookSearchResult(
                totalBookCount = 5,
                books = List(5) {
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
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SearchTabUIPreview_No_Result() {
    AppTheme {
        SearchTabUI(
            searchKeyword = "소년ㅇㄴㅇ",
            bookSearchResult = BookSearchResult(
                totalBookCount = 0,
                books = emptyList(),
            )
        )
    }
}
