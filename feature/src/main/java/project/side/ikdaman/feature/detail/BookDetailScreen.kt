package project.side.ikdaman.feature.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.navigation.ADD_BOOK_RECORD
import project.side.ikdaman.core.navigation.MAIN_ROUTE
import project.side.ikdaman.core.ui.AppText
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.core.utils.noEffectClick
import project.side.ikdaman.core.utils.oneClick
import project.side.ikdaman.core.view.BookProgressBarWithText
import project.side.ikdaman.core.view.CenterDialog
import project.side.ikdaman.core.view.CenterDialogType
import project.side.ikdaman.core.view.GradientBox
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.BookDetail
import project.side.ikdaman.domain.model.BookInfo
import project.side.ikdaman.domain.model.BookLog
import project.side.ikdaman.domain.model.BookLogItem
import project.side.ikdaman.domain.model.RecordType
import project.side.ikdaman.feature.home.HomeTextStyles
import project.side.ikdaman.feature.home.HomeViewModel

@Composable
fun BookDetailScreen(
    navController: NavController,
    bookId: String,
    isShowFirstLog: Boolean = false,
    viewModel: HomeViewModel = hiltViewModel(
        navController.getBackStackEntry(MAIN_ROUTE)
    ),
    bookDetailScreenViewModel: BookDetailScreenViewModel = hiltViewModel(
        navController.getBackStackEntry(MAIN_ROUTE)
    )
) {
    LaunchedEffect(Unit) {
        bookDetailScreenViewModel.initialize(bookId)
    }
    val deleteBookDialogState = remember { MutableTransitionState(false) }
    val deleteLogDialogState: MutableTransitionState<Boolean> = remember { MutableTransitionState(false) }
    val deleteLogItem = remember { mutableStateOf(DeleteLogItem()) }

    val bookInfo = bookDetailScreenViewModel.bookDetailState.collectAsState().value
    val bookLog = bookDetailScreenViewModel.bookLogState.collectAsState().value
    val selectedColor = viewModel.selectedColor.collectAsState().value
    val isLoading = bookDetailScreenViewModel.isLoading.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }

    BookDetailScreenUI(
        snackbarHostState = snackbarHostState,
        isLoading = isLoading,
        selectedColor = selectedColor,
        bookInfoApiResult = bookInfo,
        bookLogApiResult = bookLog,
        isShowFirstLog = isShowFirstLog,
        onBack = {
            navController.popBackStack()
        },
        onUpdateLog = { item, type, text ->
            bookDetailScreenViewModel.updateLog(bookId, item.booklogId, type, text)
        },
        onLoadMore = {
            bookDetailScreenViewModel.loadMoreLogs(bookId)
        },
        onNavigateToFirstImpression = {
            navController.navigate("$ADD_BOOK_RECORD/${RecordType.IMPRESSION}/$bookId")
        },
        onNavigateToAddRecord = {
            navController.navigate("$ADD_BOOK_RECORD/${RecordType.THINK}/$bookId")
        },
        onNavigateToCompleteRead = {
            navController.navigate("$ADD_BOOK_RECORD/${RecordType.COMPLETED}/$bookId")
        },
        onDeleteBook = {
            deleteBookDialogState.targetState = true
        },
        onDeleteLog = { logItem ->
            deleteLogDialogState.targetState = true
            deleteLogItem.value = DeleteLogItem(logItem.booklogId, logItem.type, logItem.content)
        }
    )

    CenterDialog(
        type = CenterDialogType.DELETE_BOOK,
        dialogState = deleteBookDialogState,
        onDelete = {
            viewModel.deleteItem(bookId) {
                navController.popBackStack()
            }
            deleteBookDialogState.targetState = false
        }
    )

    CenterDialog(
        type = CenterDialogType.DELETE_LOG,
        dialogState = deleteLogDialogState,
        content = deleteLogItem.value.content,
        onDelete = {
            deleteLogItem.value.apply {
                bookDetailScreenViewModel.deleteLog(bookId, logId, type)
            }
            deleteLogDialogState.targetState = false
        }
    )
    val snackBarState = bookDetailScreenViewModel.snackBarState.collectAsState().value
    val errorMessageState = bookDetailScreenViewModel.errorMessageState.collectAsState().value
    val scope = rememberCoroutineScope()
    if (snackBarState.isNotEmpty()) {
        LaunchedEffect(snackBarState) {
            scope.launch { snackbarHostState.showSnackbar(snackBarState, duration = SnackbarDuration.Short) }
        }
    } else if (errorMessageState.isNotEmpty()) {
        LaunchedEffect(errorMessageState) {
            scope.launch { snackbarHostState.showSnackbar(errorMessageState, duration = SnackbarDuration.Short) }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ConfigurationScreenWidthHeight")
@Composable
fun BookDetailScreenUI(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    isLoading: Boolean = false,
    selectedColor: Color = Palette.first,
    bookInfoApiResult: ApiResult<BookDetail> = ApiResult.Loading,
    bookLogApiResult: ApiResult<BookLog> = ApiResult.Loading,
    isShowFirstLog: Boolean = false,
    onBack: () -> Unit = {},
    onUpdateLog: (BookLogItem, String, String) -> Unit = { _, _, _ -> },
    onLoadMore: () -> Unit = {},
    onNavigateToFirstImpression: () -> Unit = {},
    onNavigateToAddRecord: () -> Unit = {},
    onNavigateToCompleteRead: () -> Unit = {},
    onDeleteBook: () -> Unit = {},
    onDeleteLog: (BookLogItem) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    LaunchedEffect(isLoading) {
        focusManager.clearFocus()
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            selectedColor,
            selectedColor.copy(alpha = 0.2f),
        )
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    containerColor = Color.Black,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 8.dp),
                ) {
                    AppText(
                        text = data.visuals.message,
                        style = DetailScreenTextStyle.snackbarStyle,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    ) {
        GradientBox(
            modifier = Modifier.fillMaxSize().noEffectClick { focusManager.clearFocus() },
            gradient = gradient
        ) {
            when (bookInfoApiResult) {
                is ApiResult.Success -> {
                    MainBody(
                        isShowFirstLog,
                        bookInfoApiResult,
                        bookLogApiResult,
                        onBack,
                        onUpdateLog,
                        onLoadMore,
                        onNavigateToFirstImpression,
                        onNavigateToAddRecord,
                        onNavigateToCompleteRead,
                        onDeleteBook,
                        onDeleteLog
                    )
                }

                is ApiResult.Error -> {
                    AppText(
                        text = bookInfoApiResult.message,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                else -> {
                    // 로딩 중일 때 보여줄 UI
                    AppText(
                        text = "로딩 중...",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun BoxScope.MainBody(
    isShowFirstLog: Boolean,
    bookInfoApiResult: ApiResult.Success<BookDetail>,
    bookLogApiResult: ApiResult<BookLog>,
    onBack: () -> Unit,
    onUpdateLog: (BookLogItem, String, String) -> Unit,
    onLoadMore: () -> Unit = {},
    onNavigateToFirstImpression: () -> Unit = {},
    onNavigateToAddRecord: () -> Unit = {},
    onNavigateToCompleteRead: () -> Unit = {},
    onDeleteItem: () -> Unit = {},
    onDeleteLog: (BookLogItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter)
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.arrow_back),
                    contentDescription = "Back",
                    modifier = Modifier
                        .padding(start = 13.dp, top = 12.dp, bottom = 12.dp)
                        .oneClick { onBack() }
                )
                AppText(text = "상세 정보", style = DetailScreenTextStyle.appBarTitle)
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.bin),
                    contentDescription = "Delete",
                    colorFilter = ColorFilter.tint(Color.Black),
                    modifier = Modifier
                        .padding(end = 14.dp)
                        .noEffectClick { onDeleteItem() }
                )
            }
        }
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                val isLoading = remember { mutableStateOf(true) }
                val book = bookInfoApiResult.data
                BookInfoView(book, isLoading)
                Spacer(Modifier.height(35.dp))
                AppText("\uD83D\uDCD6\n", style = TextStyle(fontSize = 31.sp))
                Spacer(Modifier.height(8.dp))

                val isCompleted = book.progress == 100
                if (isCompleted) {
                    AppText(
                        text = "완독한 책이에요!",
                        style = DetailScreenTextStyle.completedTextStyle,
                        height = 24.dp
                    )
                    Spacer(Modifier.height(5.dp))
                } else {
                    AppText(
                        text = book.progressText(),
                        style = DetailScreenTextStyle.progressTextBold,
                        height = 24.dp
                    )
                    AppText(
                        "독서 중인 책이에요.",
                        style = DetailScreenTextStyle.progressTextNormal,
                        height = 24.dp
                    )
                }
                Spacer(Modifier.height(15.dp))
                if (!isCompleted) {
                    Surface(
                        shadowElevation = 8.dp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .oneClick(500) {
                                onNavigateToCompleteRead()
                            }
                    ) {
                        AppText(
                            "✌다 읽었어요!",
                            style = DetailScreenTextStyle.readDone,
                            modifier = Modifier.padding(vertical = 7.dp, horizontal = 10.dp)
                        )
                    }
                    Spacer(Modifier.height(40.dp))
                }
                BookProgressBarWithText(
                    LocalConfiguration.current.screenWidthDp - 40,
                    (book.progress.toFloat() / 100),
                )
                Spacer(Modifier.height(42.dp))
                AppText(
                    "책의 첫인상",
                    style = DetailScreenTextStyle.subTitle,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(15.dp))
                val isImpressionEmpty = book.impression?.isEmpty() ?: true
                if (isImpressionEmpty) {
                    Column(
                        Modifier
                            .border(1.dp, Color(0xFFC2C2C2), RoundedCornerShape(10.dp))
                            .clip(RoundedCornerShape(10.dp))
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(vertical = 17.dp, horizontal = 20.dp)
                            .oneClick(500) {
                                onNavigateToFirstImpression()
                            },
                        horizontalAlignment = Alignment.End
                    ) {
                        AppText(
                            "처음 책을 보고 들었던 생각을 짧게 적어보세요.\n" +
                                    "독서가 마음처럼 잘되지 않을 때, 나에게 힘을 줄 거예요!",
                            style = HomeTextStyles.bottomDescription.copy(
                                color = Color(0xFF333333)
                            ),
                            maxLines = 3,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(Modifier.height(15.dp))
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.pencil),
                            contentDescription = null,
                            Modifier.size(24.dp)
                        )
                    }
                } else {
                    Column(
                        Modifier
                            .border(1.dp, Color(0xFFC2C2C2), RoundedCornerShape(10.dp))
                            .clip(RoundedCornerShape(10.dp))
                            .fillMaxWidth()
                            .animateContentSize(
                                animationSpec = tween(300)
                            )
                            .background(Color.White)
                            .padding(25.dp),
                    ) {
                        AppText(
                            text = book.impression ?: "",
                            style = DetailScreenTextStyle.impressionText,
                            maxLines = 20,
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize(
                                    animationSpec = tween(100)
                                ),
                        )
                    }
                }
                Spacer(Modifier.height(40.dp))
                AppText(
                    "이 책의 기록",
                    style = DetailScreenTextStyle.subTitle,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(15.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Black)
                        .oneClick(500) {
                            onNavigateToAddRecord()
                        }
                ) {
                    AppText(
                        "책 기록 추가하기",
                        style = DetailScreenTextStyle.addRecord,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Spacer(Modifier.height(15.dp))

            }
        }

        BookLogList(isShowFirstLog, bookLogApiResult, onUpdateLog, onLoadMore, onDeleteLog)
        item {
            Spacer(Modifier.height(40.dp))
        }
    }
}

private fun LazyListScope.BookLogList(
    isShowFirstLog: Boolean,
    bookLogApiResult: ApiResult<BookLog>,
    onUpdateLog: (BookLogItem, String, String) -> Unit,
    onLoadMore: () -> Unit,
    onDeleteLog: (BookLogItem) -> Unit
) {
    when (bookLogApiResult) {
        is ApiResult.Success -> {
            val bookLogs = bookLogApiResult.data.booklogs
            item {
                // State to track the expanded index
                val expandedIndex = remember { mutableStateOf<Int?>(null) }

                // If isShowFirstLog is true, expand the first item by default
                LaunchedEffect(isShowFirstLog, bookLogs.size) {
                    if (isShowFirstLog && bookLogs.isNotEmpty() && bookLogs[0].isNotOpenLog()) {
                        expandedIndex.value = 0
                    }
                }

                // List of book logs
                Column {
                    bookLogs.forEachIndexed { i, item ->
                        val expanded = expandedIndex.value == i
                        val isNotOpenLog = item.isNotOpenLog()

                        Column(
                            Modifier
                                .padding(horizontal = 20.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .animateContentSize(animationSpec = tween(300))
                                .noEffectClick {
                                    if (isNotOpenLog) {
                                        expandedIndex.value = if (expanded) null else i
                                    }
                                }
                                .padding(20.dp)
                        ) {
                            Row(
                                Modifier.height(24.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AppText(
                                    item.getLogString(),
                                    style = DetailScreenTextStyle.dateTextStyle,
                                    modifier = Modifier.width(105.dp)
                                )
                                AppText(
                                    item.getLogTypeText(),
                                    style = DetailScreenTextStyle.bookLogTitle,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 6.dp)
                                )
                                if (isNotOpenLog) {
                                    Image(
                                        imageVector = ImageVector.vectorResource(
                                            if (expanded) R.drawable.arrow_small_up else R.drawable.arrow_small_down
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            if (expanded) {
                                if (item.hasPage()) {
                                    Spacer(Modifier.height(15.dp))
                                    AppText(
                                        "${item.page}p",
                                        style = DetailScreenTextStyle.bookLogPageStyle,
                                        height = 20.dp
                                    )
                                }
                                Spacer(Modifier.height(20.dp))

                                val textState = remember { mutableStateOf(item.content ?: "") }

                                BasicTextField(
                                    value = textState.value,
                                    onValueChange = {
                                        if (it.length <= 500) {
                                            textState.value = it
                                        }
                                    },
                                    textStyle = DetailScreenTextStyle.bookLogContentStyle,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(Modifier.height(20.dp))
                                if (item.isNotImpression()) {
                                    Row(
                                        horizontalArrangement = Arrangement.End,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Box(
                                            Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color.Black)
                                                .oneClick(1000) {
                                                    if (item.content != textState.value) {
                                                        onUpdateLog(item, item.type, textState.value)
                                                    }
                                                }
                                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            AppText("저장", style = DetailScreenTextStyle.bookLogButtonStyle)
                                        }
                                        Spacer(Modifier.width(5.dp))
                                        Box(
                                            Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0xFF858585))
                                                .clickable {
                                                    expandedIndex.value = null
                                                    onDeleteLog(item)
                                                }
                                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            AppText("삭제", style = DetailScreenTextStyle.bookLogButtonStyle)
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(5.dp))
                        Log.i("BookDetailScreen", "onLoad?: $i, ${bookLogs.size}")
                        if (i == bookLogs.lastIndex - 2) {
                            onLoadMore()
                        }
                    }
                }
            }
        }
        is ApiResult.Loading -> {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp),
                    color = Color.Black
                )
            }
        }
        is ApiResult.Error -> {
            item {
                AppText(
                    text = bookLogApiResult.message,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun BookInfoView(
    book: BookDetail,
    isLoading: MutableState<Boolean>
) {
    Box(
        Modifier.padding(top = 10.dp)
    ) {
        Row(
            Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(17.dp)
        ) {
            val bookInfo = book.bookInfo
            Box {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = bookInfo.coverImage,
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
                    modifier = Modifier.size(91.dp, 130.dp)
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
            Spacer(Modifier.width(15.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                AppText(
                    bookInfo.title,
                    maxLines = 99,
                    style = DetailScreenTextStyle.bookTitle,
                )
                Spacer(Modifier.height(22.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText(
                        "작가",
                        style = DetailScreenTextStyle.bookInfoKey,
                        modifier = Modifier.width(50.dp)
                    )
                    AppText(
                        bookInfo.author,
                        style = DetailScreenTextStyle.bookInfoValue,
                        modifier = Modifier.basicMarquee()
                    )
                }
                Spacer(Modifier.height(5.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText(
                        "출판사",
                        style = DetailScreenTextStyle.bookInfoKey,
                        modifier = Modifier.width(50.dp)
                    )
                    AppText(
                        bookInfo.publisher,
                        style = DetailScreenTextStyle.bookInfoValue
                    )
                }
                Spacer(Modifier.height(5.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText(
                        "총 페이지",
                        style = DetailScreenTextStyle.bookInfoKey,
                        modifier = Modifier.width(50.dp)
                    )
                    AppText(
                        "${bookInfo.totalPage}",
                        style = DetailScreenTextStyle.bookInfoValue
                    )
                }
                Spacer(Modifier.height(15.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText(
                        "도서정보 알라딘 제공",
                        style = DetailScreenTextStyle.aladinTextKey
                    )
                    Spacer(Modifier.width(9.dp))
                    val context = LocalContext.current
                    val url = "https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=${bookInfo.itemId}&amp;partner=openAPI&amp;start=api"
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.oneClick(2000) {
                            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                            context.startActivity(intent)
                        }
                    ) {
                        AppText(
                            "알라딘에서 보기",
                            style = DetailScreenTextStyle.aladinTextValue,
                            modifier = Modifier.padding(vertical = 3.dp)
                        )
                        Box(
                            Modifier
                                .background(Color.Black)
                                .width(73.dp)
                                .height(1.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 1394)
@Composable
fun BookDetailScreenUIPreview() {
    AppTheme {
        BookDetailScreenUI(
            selectedColor = Palette.second,
            bookInfoApiResult = ApiResult.Success(
                BookDetail(
                    mybookId = "1",
                    bookInfo = BookInfo(
                        title = "(개정판) 소년이 온다",
                        coverImage = "https://picsum.photos/182/260?random=1",
                        author = "한강",
                        publisher = "더스토리",
                        totalPage = 279,
                    ),
                    startDate = "2025-06-01",
                    nowPage = 155,
                    progress = 100,
                    impression = "네가 죽은 뒤 장례식을 치르지 못해, 내 삶이 장례식이 되었다.\n네가 방수 모포에 싸여 청소차에 실려간 뒤에.\n용서할 수 없는 물줄기가 번쩍이며 분수대에서 뿜어져나온 뒤에.",
                )
            ),
            bookLogApiResult = ApiResult.Success(
                BookLog(
                    booklogs = listOf(

                        BookLogItem(
                            booklogId = 3,
                            type = "REVIEW",
                            content = "책을 다 읽고 나서 리뷰를 작성했어.",
                            loggedDate = "2025-06-03T12:00:00.123123",
                            page = 150
                        ),
                        BookLogItem(
                            booklogId = 2,
                            type = "THINK",
                            content = "이 책은 정말 감동적이야.",
                            loggedDate = "2025-06-01T12:00:00.123123",
                            page = 50
                        ),
                        BookLogItem(
                            booklogId = 1,
                            type = "IMPRESSION",
                            content = "첫인상이 너무 좋았어.",
                            loggedDate = "2025-06-02T12:00:00.123123",
                            page = 100
                        ),
                        BookLogItem(
                            booklogId = 1,
                            type = "OPEN",
                            content = "첫인상이 너무 좋았어.",
                            loggedDate = "2025-05-31T12:00:00.123123",
                            page = 100
                        ),
                    ),
                    hasNext = false
                )
            )
        )
    }
}