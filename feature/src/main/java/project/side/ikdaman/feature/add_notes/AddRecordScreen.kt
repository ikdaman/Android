@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import project.side.ikdaman.core.ui.AppText
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.utils.oneClick
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.BookDetail

@Composable
fun AddRecordScreen(
    navController: NavController,
    viewModel: AddRecordViewModel = hiltViewModel(),
    recordType: RecordType,
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
                val id = result.data.mybookId
                viewModel.addMiddleRecord(id, text, page) {
                    navController.popBackStack()
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
    recordType: RecordType,
    textState: MutableState<String> = remember { mutableStateOf("") },
    result: ApiResult<BookDetail> = ApiResult.Loading,
    onBack: () -> Unit = {},
    onConfirmFirstImpression: (String) -> Unit = {},
    onConfirmMiddleRecord: (String, Int) -> Unit = { _, _ -> },
    onConfirmCompletedRecord: (String) -> Unit = {},
) {
    val pageState: MutableState<Int?> = remember { mutableStateOf(null) }
    val recordTypeState = remember { mutableStateOf(recordType) }
    Scaffold(
        topBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 12.dp)
                    .padding(start = 13.dp)
                    .height(50.dp)
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.arrow_back),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterStart).oneClick { onBack() }
                )
                val titleText = when (recordTypeState.value) {
                    RecordType.FIRST -> "기록 추가하기"
                    RecordType.MIDDLE -> "기록 추가하기"
                    RecordType.FINAL -> "완독 기록하기"
                }
                AppText(
                    titleText,
                    style = AddRecordTextStyles.appBarTitleTextStyle,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    ) {
        Column(Modifier.fillMaxHeight()) {
            Spacer(Modifier.height(50.dp))
            when (recordTypeState.value) {
                RecordType.FIRST -> {
                    FirstImpressionView(
                        textState = textState,
                        onConfirm = {
                            onConfirmFirstImpression(it)
                        }
                    )
                }

                RecordType.MIDDLE -> {
                    AddMiddleRecordView(
                        textState = textState,
                        pageState = pageState,
                        result = result,
                        onConfirm = { text, page ->
                            onConfirmMiddleRecord(text, page)
                        },
                        onNavigateToCompleteView = {
                            recordTypeState.value = RecordType.FINAL
                        }
                    )
                }

                RecordType.FINAL -> {
                    ReadCompleteView(
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
        AddRecordScreenUI(recordType = RecordType.FIRST)
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun AddRecordScreenMiddlePreview() {
    AppTheme {
        AddRecordScreenUI(recordType = RecordType.MIDDLE)
    }
}

@Preview(showBackground = true)
@Composable
fun AddRecordScreenFinalPreview() {
    AppTheme {
        AddRecordScreenUI(recordType = RecordType.FINAL)
    }
}