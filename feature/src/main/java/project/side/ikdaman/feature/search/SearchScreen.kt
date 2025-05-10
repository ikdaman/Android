package project.side.ikdaman.feature.search

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavController
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.navigation.BOOK_EDIT_ROUTE
import project.side.ikdaman.core.navigation.MAIN_ROUTE
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.core.ui.PretendardFontFamily
import project.side.ikdaman.domain.model.BookItem
import project.side.ikdaman.domain.model.BookSearch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = hiltViewModel(
        navController.getBackStackEntry(MAIN_ROUTE)
    )
) {
    val searchKeyword by remember { mutableStateOf("") }
    val bookSearch by remember { mutableStateOf(BookSearch()) }

    SearchScreenUI(
        onBack = { navController.popBackStack() },
        onNavigateToEditScreen = {
            navController.navigate(BOOK_EDIT_ROUTE)
        },
        onSearchKeywordChange = {
            searchViewModel.searchBookWithTitle(it)
        },
        searchKeyword = searchKeyword,
        bookSearch = bookSearch,
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreenUI(
    onNavigateToEditScreen: () -> Unit = {},
    onSearchKeywordChange: (String) -> Unit = {},
    searchKeyword: String = "",
    onBack: () -> Unit = {},
    bookSearch: BookSearch = BookSearch(),
) {
    val selectedColor by remember { mutableStateOf(Palette.first) }
    val backgroundGradientModifier = remember {
        Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        selectedColor,
                        selectedColor.copy(alpha = 0.2f),
                    )
                )
            )
    }

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
        Column(
            modifier = backgroundGradientModifier
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SearchTextField(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(top = 24.dp),
                value = searchKeyword,
                onValueChange = onSearchKeywordChange
            )
            SearchResultScreen(
                searchKeyword = searchKeyword,
                result = bookSearch
            )
        }
    }
}

@Composable
private fun SearchTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp
        )
    ) { innerTextField ->
        Box(
            modifier = Modifier
                .heightIn(min = 48.dp)
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(5.dp),
                    color = Color.White
                )
        ) {
            if (value.isEmpty()) {
                Text(
                    "책 제목을 검색해주세요.", color = Color(0xff989898),
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
}

@Composable
private fun SearchResultScreen(
    searchKeyword: String,
    result: BookSearch
) {
    if (result.totalBookCount == 0) {
        NoSearchResultScreen(searchKeyword)
    } else {
        LazyColumn(modifier = Modifier.padding(top = 24.dp)) {
            items(result.books) { item ->
                SearchResultItem(item)
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
private fun NoSearchResultScreen(searchKeyword: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 174.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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

@Composable
private fun SearchResultItem(
    bookItem: BookItem
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp)
    ) {
        Row {
            // todo BookItem.cover 사용하기
//            AsyncImage(
//                model = bookItem.cover,
//                contentDescription = "Book Cover",
//                modifier = Modifier
//                    .size(width = 80.dp, height = 114.dp)
//            )
            Image(
                painter = painterResource(R.drawable.sample_book_cover),
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
        SearchResultAddButton(modifier = Modifier.align(Alignment.BottomEnd))

    }
}

@Composable
private fun SearchResultAddButton(modifier: Modifier) {
    Button(
        onClick = {},
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
@Preview(showBackground = true)
private fun SearchScreenUIPreview() {
    AppTheme {
        SearchScreenUI(
            searchKeyword = "소년",
            bookSearch = BookSearch(
                totalBookCount = 5,
                books = List(5) {
                    BookItem(
                        title = "소년이 온다(개정판)",
                        author = "한강",
                        cover = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/4808936434120.jpg",
                        isbn = ""
                    )
                }
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SearchScreenNUIPreview_No_Result() {
    AppTheme {
        SearchScreenUI(
            searchKeyword = "소년ㅇㄴㅇ",
            bookSearch = BookSearch(
                totalBookCount = 0,
                books = emptyList(),
            )
        )
    }
}