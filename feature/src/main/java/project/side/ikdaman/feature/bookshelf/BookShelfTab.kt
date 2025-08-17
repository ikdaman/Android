package project.side.ikdaman.feature.bookshelf

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.distinctUntilChanged
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.navigation.BOOK_DETAIL_ROUTE
import project.side.ikdaman.core.navigation.SEARCH_ROUTE
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.core.utils.noEffectClick
import project.side.ikdaman.domain.model.BookShelfItem
import project.side.ikdaman.feature.bookshelf.BookShelfViewModel.BookShelfFilter
import project.side.ikdaman.feature.search.SearchTextField
import kotlin.math.ceil

@SuppressLint("UnrememberedMutableState")
@Composable
fun BookShelfTab(
    navController: NavController,
    mainNavController: NavController,
    viewModel: BookShelfViewModel = hiltViewModel()
) {
    val selectedColor = viewModel.selectedColor.collectAsState().value
    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val lazyListState = rememberLazyListState()
    val keyword = viewModel.keyword.collectAsState().value
    val prevFilter = remember { mutableStateOf(uiState.filter) }

    val shouldLoadMore by derivedStateOf {
        if (uiState.books.isEmpty()) return@derivedStateOf false
        val lastVisibleRow = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
        val totalRows = ceil(uiState.books.size / 3.0).toInt()
        lastVisibleRow >= totalRows - 1 && !uiState.isLoading && uiState.nowPage < uiState.totalPage
    }

    LaunchedEffectLoadMoreBooks(shouldLoadMore) {
        viewModel.getBooks(
            isLoadMore = true,
            page = uiState.nowPage + 1
        )
    }

    LaunchedEffect(uiState.filter) {
        if (prevFilter.value != uiState.filter) {
            lazyListState.scrollToItem(0)
            prevFilter.value = uiState.filter
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    BookShelfTabUI(
        focusManager = focusManager,
        lazyListState = lazyListState,
        books = uiState.books,
        isLoading = uiState.isLoading,
        isCompleted = uiState.isCompleted,
        keyword = keyword ?: "",
        onKeywordChanged = { viewModel.onKeywordChanged(it) },
        selectedColor = selectedColor,
        selectedFilter = uiState.filter,
        onFilterChanged = { filter ->
            viewModel.onFilterChanged(filter)
            viewModel.getBooks()
        },
        onNavigateToBookDetail = {
            focusManager.clearFocus()
            navController.navigate(it)
        },
        onNavigateToBookSearch = {
            focusManager.clearFocus()
            mainNavController.navigate(SEARCH_ROUTE)
        },
    )
}

@Composable
fun LaunchedEffectLoadMoreBooks(
    shouldLoadMore: Boolean,
    onLoadMoreBooks: () -> Unit = {}
) {
    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore }
            .distinctUntilChanged()
            .collect { shouldLoad ->
                if (shouldLoad) {
                    onLoadMoreBooks()
                }
            }
    }
}

@Composable
fun BookShelfTabUI(
    focusManager: FocusManager = LocalFocusManager.current,
    lazyListState: LazyListState = rememberLazyListState(),
    isLoading: Boolean = false,
    isCompleted: Boolean = false,
    books: List<BookShelfItem> = emptyList(),
    keyword: String = "",
    onKeywordChanged: (String) -> Unit = {},
    selectedColor: Color = Palette.first,
    selectedFilter: BookShelfFilter = BookShelfFilter.ALL,
    onFilterChanged: (BookShelfFilter) -> Unit = {},
    onNavigateToBookDetail: (String) -> Unit = {},
    onNavigateToBookSearch: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(selectedColor)
            .statusBarsPadding()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
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
                focusManager = focusManager,
                isCamera = false,
                searchText = keyword,
                onSearchTextChanged = { onKeywordChanged(it) }
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
            Crossfade(
                targetState = books,
                animationSpec = tween(durationMillis = 300), // 전환 속도
                label = "BookListFade"
            ) { currentBooks ->
                BookShelfList(
                    modifier = Modifier.weight(1f),
                    isEmpty = !isLoading && isCompleted && books.isEmpty(),
                    books = currentBooks,
                    selectedColor = selectedColor,
                    selectedFilter = selectedFilter,
                    lazyListState = lazyListState,
                    onNavigateToBookSearch = onNavigateToBookSearch,
                    onNavigateToBookDetail = onNavigateToBookDetail
                )
            }

        }
    }
}

@Composable
fun BookShelfList(
    modifier: Modifier = Modifier,
    isEmpty: Boolean,
    books: List<BookShelfItem>,
    selectedColor: Color,
    selectedFilter: BookShelfFilter,
    lazyListState: LazyListState,
    onNavigateToBookSearch: () -> Unit,
    onNavigateToBookDetail: (String) -> Unit
) {
    if (isEmpty) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(175.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White.copy(alpha = 0.6f))
                .clickable { onNavigateToBookSearch() }
        ) {
            val text = when (selectedFilter) {
                BookShelfFilter.COMPLETE -> "+\n" +
                        "완독한 책이 없어요.\n" +
                        "읽다만 책을 읽어볼까요?"

                else -> "+\n" +
                        "가지고 있는 책이 없어요.\n" +
                        "독서를 추가해보세요 \uD83E\uDD13\uFE0F"
            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = text,
                style = BookShelfTextStyle.emptyBookShelfText,
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            state = lazyListState,
            modifier = modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            contentPadding = PaddingValues(bottom = 56.dp)
        ) {
            val rowCount = ceil(books.size / 3.0).toInt()
            items(rowCount) { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (col in 0..<3) {
                        val book = books.getOrNull(row * 3 + col)

                        if (book != null) {
                            BookItem(book.isCompleted, book.mybookId, book.coverImage) {
                                onNavigateToBookDetail(it)
                            }
                        } else {
                            Spacer(modifier = Modifier.size(100.dp, 160.dp))
                        }
                    }
                }
                BookShelf(selectedColor = selectedColor)
                Spacer(modifier = Modifier.height(50.dp))
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

@Stable
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
    bookImage: String = "",
    onNavigateTo: (String) -> Unit = {}
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
            .clickable { onNavigateTo("$BOOK_DETAIL_ROUTE/$myBookId/false") }
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
        BookShelfTabUI(isLoading = true, books = listOf(BookShelfItem()))
    }
}