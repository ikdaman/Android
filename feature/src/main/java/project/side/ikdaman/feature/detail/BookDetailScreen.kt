package project.side.ikdaman.feature.detail

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.navigation.MAIN_ROUTE
import project.side.ikdaman.core.ui.AppText
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.core.utils.oneClick
import project.side.ikdaman.core.view.GradientBox
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.BookDetail
import project.side.ikdaman.domain.model.BookInfo
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
        bookDetailScreenViewModel.getBookDetail(bookId)
    }

    val bookInfo = bookDetailScreenViewModel.bookDetailState.collectAsState().value

    val selectedColor = viewModel.selectedColor.collectAsState().value
    BookDetailScreenUI(
        selectedColor = selectedColor,
        apiResult = bookInfo,
        onBack = {
            navController.popBackStack()
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BookDetailScreenUI(
    selectedColor: Color = Palette.first,
    apiResult: ApiResult<BookDetail> = ApiResult.Loading,
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
            if (apiResult is ApiResult.Success) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier.align(Alignment.TopCenter)
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
                    Box(
                        Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp)
                    ) {
                        Row(
                            Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .padding(17.dp)
                        ) {
                            val bookInfo = apiResult.data.bookInfo
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
                                AppText(bookInfo.title, maxLines = 3, style = DetailScreenTextStyle.bookTitle)
                                Spacer(Modifier.height(22.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AppText("작가", style = DetailScreenTextStyle.bookInfoKey, modifier = Modifier.width(50.dp))
                                    AppText(bookInfo.author, style = DetailScreenTextStyle.bookInfoValue)
                                }
                                Spacer(Modifier.height(5.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AppText("출판사", style = DetailScreenTextStyle.bookInfoKey, modifier = Modifier.width(50.dp))
                                    AppText(bookInfo.publisher, style = DetailScreenTextStyle.bookInfoValue)
                                }
                                Spacer(Modifier.height(5.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AppText("총 페이지", style = DetailScreenTextStyle.bookInfoKey, modifier = Modifier.width(50.dp))
                                    AppText("${bookInfo.totalPage}", style = DetailScreenTextStyle.bookInfoValue)
                                }
                                Spacer(Modifier.height(15.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AppText("도서정보 알라딘 제공", style = DetailScreenTextStyle.aladinTextKey)
                                    Spacer(Modifier.width(9.dp))
                                    Column(verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally) {
                                        AppText("알라딘에서 보기", style = DetailScreenTextStyle.aladinTextValue, modifier = Modifier.padding(vertical = 3.dp))
                                        Box(Modifier
                                            .background(Color.Black)
                                            .width(73.dp)
                                            .height(1.dp))
                                    }
                                }
                            }
                        }
                    }
                    
                }
            } else if (apiResult is ApiResult.Error) {
                AppText(
                    text = apiResult.message,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                // 로딩 중일 때 보여줄 UI
                AppText(
                    text = "로딩 중...",
                    modifier = Modifier.padding(16.dp)
                )
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
            apiResult = ApiResult.Success(
                BookDetail(
                    mybookId = "1",
                    bookInfo = BookInfo(
                        title = "(개정판) 소년이 온다",
                        coverImage = "https://picsum.photos/182/260?random=1",
                        author = "윤동주",
                        publisher = "더스토리",
                        totalPage = 279
                    ),
                    startDate = "2025-06-01",
                    nowPage = 155,
                    progress = 42,
                    impression = "이 책은 정말 감동적이었어요. 윤동주의 시는 언제 읽어도 마음에 와닿습니다.",
                )
            )
        )
    }
}