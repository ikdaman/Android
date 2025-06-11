package project.side.ikdaman.feature.detail

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.navigation.MAIN_ROUTE
import project.side.ikdaman.core.ui.AppText
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.core.utils.oneClick
import project.side.ikdaman.core.view.BookProgressBarWithText
import project.side.ikdaman.core.view.GradientBox
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.BookDetail
import project.side.ikdaman.domain.model.BookInfo
import project.side.ikdaman.domain.model.BookLog
import project.side.ikdaman.feature.home.HomeTextStyles
import project.side.ikdaman.feature.home.HomeViewModel

@Composable
fun BookDetailScreen(
    navController: NavController,
    bookId: String,
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

    val bookInfo = bookDetailScreenViewModel.bookDetailState.collectAsState().value
    val bookLog = bookDetailScreenViewModel.bookLogState.collectAsState().value

    val selectedColor = viewModel.selectedColor.collectAsState().value
    BookDetailScreenUI(
        selectedColor = selectedColor,
        bookInfoApiResult = bookInfo,
        bookLogApiResult = bookLog,
        onBack = {
            navController.popBackStack()
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ConfigurationScreenWidthHeight")
@Composable
fun BookDetailScreenUI(
    selectedColor: Color = Palette.first,
    bookInfoApiResult: ApiResult<BookDetail> = ApiResult.Loading,
    bookLogApiResult: ApiResult<BookLog> = ApiResult.Loading,
    onBack: () -> Unit = {}
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            selectedColor,
            selectedColor.copy(alpha = 0.2f),
        )
    )

    Scaffold {
        GradientBox(
            modifier = Modifier.fillMaxSize(),
            gradient = gradient
        ) {
            when (bookInfoApiResult) {
                is ApiResult.Success -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(horizontal = 20.dp)
                    ) {
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
                                    .oneClick { }
                            )
                            AppText(text = "상세 정보", style = DetailScreenTextStyle.appBarTitle)
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.bin),
                                contentDescription = "Delete",
                                colorFilter = ColorFilter.tint(Color.Black),
                                modifier = Modifier.padding(end = 14.dp)
                            )
                        }

                        val isLoading = remember { mutableStateOf(true) }
                        val book = bookInfoApiResult.data
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
                                Column {
                                    AppText(
                                        bookInfo.title,
                                        maxLines = 3,
                                        style = DetailScreenTextStyle.bookTitle
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
                                        Column(
                                            verticalArrangement = Arrangement.SpaceBetween,
                                            horizontalAlignment = Alignment.CenterHorizontally
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
                        Spacer(Modifier.height(35.dp))
                        AppText("\uD83D\uDCD6\n", style = TextStyle(fontSize = 31.sp))
                        Spacer(Modifier.height(8.dp))
                        AppText(
                            text = book.progressText(),
                            style = DetailScreenTextStyle.progressTextBold,
                            modifier = Modifier.height(24.dp)
                        )
                        AppText(
                            "독서 중인 책이에요.",
                            style = DetailScreenTextStyle.progressTextNormal,
                            modifier = Modifier.height(24.dp)
                        )
                        Spacer(Modifier.height(15.dp))
                        Surface(
                            shadowElevation = 8.dp,
                            modifier = Modifier.clip(RoundedCornerShape(20.dp))
                        ) {
                            AppText(
                                "✌다 읽었어요!",
                                style = DetailScreenTextStyle.readDone,
                                modifier = Modifier.padding(vertical = 7.dp, horizontal = 10.dp)
                            )
                        }
                        Spacer(Modifier.height(52.dp))
                        BookProgressBarWithText(
                            LocalConfiguration.current.screenWidthDp,
                            (book.progress.toFloat() / 100),
                            modifier = Modifier.padding(horizontal = 20.dp)
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
                                    .padding(vertical = 17.dp, horizontal = 20.dp),
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
                        ) {
                            AppText(
                                "책 기록 추가하기",
                                style = DetailScreenTextStyle.addRecord,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        Spacer(Modifier.height(15.dp))
                        when (bookLogApiResult) {
                            is ApiResult.Success -> {
                                val bookLog = bookLogApiResult.data
                            }
                            is ApiResult.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(16.dp),
                                    color = Color.Black
                                )
                            }
                            is ApiResult.Error -> {
                                AppText(
                                    text = bookLogApiResult.message,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
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
                        author = "윤동주 윤동주 윤동주 윤동주 윤동주 윤동주 윤동주 윤동주",
                        publisher = "더스토리",
                        totalPage = 279,
                    ),
                    startDate = "2025-06-01",
                    nowPage = 155,
                    progress = 42,
                    impression = "네가 죽은 뒤 장례식을 치르지 못해, 내 삶이 장례식이 되었다.\n네가 방수 모포에 싸여 청소차에 실려간 뒤에.\n용서할 수 없는 물줄기가 번쩍이며 분수대에서 뿜어져나온 뒤에.",
                )
            )
        )
    }
}