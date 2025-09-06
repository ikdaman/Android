package project.side.ikdaman.feature.add_notes

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.navigation.ADD_BOOK_RECORD
import project.side.ikdaman.core.navigation.BOOK_DETAIL_ROUTE
import project.side.ikdaman.core.ui.AppText
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.utils.oneClick
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.BookDetail
import project.side.ikdaman.domain.model.BookInfo
import project.side.ikdaman.domain.model.RecordType

@Composable
fun AddRecordScreen(
    navController: NavController,
    viewModel: AddRecordViewModel = hiltViewModel(),
    isShowFirstLog: Boolean = false,
    recordType: String,
    bookId: String,
) {
    val textState = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getBookInfo(bookId)
    }

    val result = viewModel.bookDetailState.collectAsState().value

    AddRecordScreenUI(
        recordType = recordType,
        textState = textState,
        result = result,
        onBack = {
            navController.popBackStack()
        },
        onConfirmFirstImpression = {
            if (result is ApiResult.Success) {
                val id = result.data.mybookId
                viewModel.addFirstImpression(id, it) {
                    navController.popBackStack()
                }
            }
        },
        onConfirmMiddleRecord = { text, page ->
            if (result is ApiResult.Success) {
                viewModel.addMiddleRecord(result.data, text, page) {
                    if (isShowFirstLog) {
                        navController.navigate("${BOOK_DETAIL_ROUTE}/$bookId/true") {
                            popUpTo("$ADD_BOOK_RECORD/${RecordType.THINK}/$bookId?isShowFirstLog=${true}") {
                                inclusive = true
                            }
                        }
                    } else {
                        navController.popBackStack()
                    }
                }
            }
        },
        onConfirmCompletedRecord = {
            if (result is ApiResult.Success) {
                val id = result.data.mybookId
                viewModel.addCompletedRecord(id, it) {
                    navController.popBackStack()
                }
            }
        },
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddRecordScreenUI(
    recordType: String,
    textState: MutableState<String> = remember { mutableStateOf("") },
    result: ApiResult<BookDetail> = ApiResult.Loading,
    onBack: () -> Unit = {},
    onConfirmFirstImpression: (String) -> Unit = {},
    onConfirmMiddleRecord: (String, Int) -> Unit = { _, _ -> },
    onConfirmCompletedRecord: (String) -> Unit = {},
) {
    val pageState: MutableState<Int?> = remember { mutableStateOf(null) }
    val recordTypeState = remember { mutableStateOf(recordType) }
    Scaffold {
        Column(Modifier.background(Color.White).fillMaxHeight()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 12.dp)
                    .padding(start = 13.dp)
                    .statusBarsPadding()
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.arrow_back),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .oneClick { onBack() }
                )
                val titleText = when (recordTypeState.value) {
                    RecordType.IMPRESSION -> "기록 추가하기"
                    RecordType.THINK -> "기록 추가하기"
                    RecordType.COMPLETED -> "완독 기록하기"
                    else -> ""
                }
                AppText(
                    titleText,
                    style = AddRecordTextStyles.appBarTitleTextStyle,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(Modifier.height(10.dp))
            when (recordTypeState.value) {
                RecordType.IMPRESSION -> {
                    FirstImpressionView(
                        modifier = Modifier.navigationBarsPadding(),
                        textState = textState,
                        result = result,
                        onConfirm = {
                            onConfirmFirstImpression(it)
                        }
                    )
                }

                RecordType.THINK -> {
                    AddThinkView(
                        modifier = Modifier.navigationBarsPadding(),
                        textState = textState,
                        pageState = pageState,
                        result = result,
                        onConfirm = { text, page ->
                            onConfirmMiddleRecord(text, page)
                        },
                        onNavigateToCompleteView = {
                            recordTypeState.value = RecordType.COMPLETED
                        }
                    )
                }

                RecordType.COMPLETED -> {
                    ReadCompleteView(
                        modifier = Modifier.navigationBarsPadding(),
                        textState = textState,
                        onConfirm = {
                            onConfirmCompletedRecord(it)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddRecordScreenFirstPreview() {
    AppTheme {
        AddRecordScreenUI(
            recordType = RecordType.IMPRESSION,
            result = ApiResult.Success(
                BookDetail(
                    bookInfo = BookInfo(
                        title = "책 제목",
                        author = "저자 이름",
                    )

                )
            )
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun AddRecordScreenMiddlePreview() {
    AppTheme {
        AddRecordScreenUI(
            recordType = RecordType.THINK,
            result = ApiResult.Success(
                BookDetail(
                    bookInfo = BookInfo(
                        title = "책 제목",
                        author = "저자 이름",
                    )

                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddRecordScreenFinalPreview() {
    AppTheme {
        AddRecordScreenUI(
            recordType = RecordType.COMPLETED,
            result = ApiResult.Success(
                BookDetail(
                    bookInfo = BookInfo(
                        title = "책 제목",
                        author = "저자 이름",
                    )

                )
            )
        )
    }
}