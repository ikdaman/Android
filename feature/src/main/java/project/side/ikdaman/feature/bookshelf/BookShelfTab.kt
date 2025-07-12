package project.side.ikdaman.feature.bookshelf

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.core.utils.noEffectClick
import project.side.ikdaman.domain.model.BookShelfItem
import project.side.ikdaman.feature.bookshelf.BookShelfViewModel.BookShelfFilter
import project.side.ikdaman.feature.search.SearchTextField
import kotlin.math.ceil

@Composable
fun BookShelfTab(navController: NavController, viewModel: BookShelfViewModel = hiltViewModel()) {
    val selectedColor = viewModel.selectedColor.collectAsState().value
    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current
    val selectedFilter = remember { mutableStateOf(BookShelfFilter.ALL) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    BookShelfTabUI(
        onNavigateTo = {
            navController.navigate(it)
        },
        selectedColor = selectedColor,
        isLoading = uiState.isLoading,
        totalPage = uiState.totalPage,
        books = uiState.books,
        totalBooks = uiState.totalBooks,
        selectedFilter = selectedFilter.value,
        onFilterChanged = { filter ->
            selectedFilter.value = filter
            viewModel.getBooks(filter)
        }
    )
}

@Composable
fun BookShelfTabUI(
    onNavigateTo: (String) -> Unit = {},
    selectedColor: Color = Palette.first,
    isLoading: Boolean = false,
    totalPage: Int = 1,
    books: List<BookShelfItem> = emptyList(),
    totalBooks: Int = 0,
    selectedFilter: BookShelfFilter = BookShelfFilter.ALL,
    onFilterChanged: (BookShelfFilter) -> Unit = {},
) {
    val pagerState = rememberPagerState(pageCount = { totalPage })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(selectedColor)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .zIndex(1f)
            )
        }
        Column {
            SearchTextField(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 35.dp, bottom = 40.dp),
                searchText = "",
                onSearchTextChanged = {}
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 17.dp),
                horizontalArrangement = Arrangement.End
            ) {
                FilterText(
                    text = "전체",
                    isSelected = selectedFilter == BookShelfFilter.ALL,
                    onClick = { onFilterChanged(BookShelfFilter.ALL) }
                )
                FilterText(
                    text = "\uD83C\uDFB5 완독한 책",
                    isSelected = selectedFilter == BookShelfFilter.COMPLETE,
                    onClick = { onFilterChanged(BookShelfFilter.COMPLETE) }
                )
                FilterText(
                    text = "\uD83D\uDCD6 독서중인 책",
                    isSelected = selectedFilter == BookShelfFilter.PROGRESS,
                    onClick = { onFilterChanged(BookShelfFilter.PROGRESS) }
                )
            }
            HorizontalPager(state = pagerState) { page ->
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Log.d("yewon", "$totalBooks")
                    val totalBookShelfLine = ceil(totalBooks / 3.0).toInt()
                    val lastBooks = totalBooks % 3

                    if (totalBooks == 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .height(175.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(alpha = 0.6f))
                        ) {
                            val text = when (selectedFilter) {
                                BookShelfFilter.ALL -> "+\n" +
                                        "가지고 있는 책이 없어요.\n" +
                                        "독서를 추가해보세요 \uD83E\uDD13\uFE0F"

                                BookShelfFilter.COMPLETE -> "+\n" +
                                        "완독한 책이 없어요.\n" +
                                        "읽다만 책을 읽어볼까요?"

                                BookShelfFilter.PROGRESS -> ""
                            }

                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = text,
                                style = BookShelfTextStyle.emptyBookShelfText,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        for (i in 0..<totalBookShelfLine) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                for (j in 0..<lastBooks) {
                                    val book = books[i * 3 + j]
                                    BookItem(true, book.mybookId, book.coverImage)
                                }
                                if (lastBooks != 0) {
                                    for (j in 0..<(3 - lastBooks)) {
                                        Spacer(modifier = Modifier.size(100.dp, 160.dp))
                                    }
                                }
                            }
                            BookShelf(selectedColor = selectedColor)
                            Spacer(modifier = Modifier.height(30.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterText(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Text(
        modifier = Modifier
            .padding(start = 10.dp)
            .noEffectClick { onClick() },
        text = text,
        style = if (isSelected) BookShelfTextStyle.FilterText.copy(fontWeight = FontWeight.Bold) else
            BookShelfTextStyle.FilterText.copy(color = Color.White.copy(alpha = 0.6f)),
    )
}

@Composable
fun BookShelf(modifier: Modifier = Modifier, selectedColor: Color = Palette.first) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(23.dp)
            .innerShadow(
                shape = RectangleShape,
                color = selectedColor,
                blur = 8.dp
            )
            .background(color = Color.White.copy(alpha = 0.3f))
    )
}

@Preview(showBackground = true)
@Composable
fun BookItem(
    isCompleted: Boolean = false,
    myBookId: Long = 0,
    bookImage: String = ""
) {
    val isLoading = remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .size(100.dp, 160.dp)
            .background(Color.White)
            .dropShadow(
                color = Color.Black.copy(alpha = 0.1f),
                offsetX = (-5).dp,
                offsetY = 0.dp,
                blurRadius = 4.dp
            )
    ) {
        if (isCompleted) {
            Image(
                painter = painterResource(R.drawable.book_mark),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .zIndex(1f)
            )
        }
        if (bookImage.isEmpty()) {
            Image(
                painter = painterResource(R.drawable.no_image),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = bookImage,
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
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
                if (isLoading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(30.dp),
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun BookShelfTabUIPreview() {
    AppTheme {
        BookShelfTabUI(isLoading = true, books = listOf(BookShelfItem()), totalBooks = 0)
    }
}