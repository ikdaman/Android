@file:OptIn(ExperimentalMaterial3Api::class)

package project.side.ikdaman.feature.add_notes

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.ui.AppText
import project.side.ikdaman.core.ui.AppTheme

@Composable
fun AddRecordScreen(
    navController: NavController,
    recordType: RecordType,
    onBack: () -> Unit = {},
    onNavigateToEditScreen: () -> Unit = {},
) {
    val textState = remember { mutableStateOf("") }
    AddRecordScreenUI(
        recordType = recordType,
        textState = textState,
        onBack = onBack,
        onNavigateToEditScreen = onNavigateToEditScreen
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddRecordScreenUI(
    recordType: RecordType,
    textState: MutableState<String> = remember { mutableStateOf("") },
    onBack: () -> Unit = {},
    onNavigateToEditScreen: () -> Unit = {},
) {
    val pageState = remember { mutableStateOf(0) }
    Scaffold(
        topBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .padding(start = 13.dp)
                    .height(50.dp)
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.arrow_back),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                val titleText = when (recordType) {
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
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black)
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable { }
            ) {
                Text(
                    "확인",
                    style = AddRecordTextStyles.buttonTextStyle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
            }
        }
    ) {
        Column {
            Spacer(Modifier.height(50.dp))
            when (recordType) {
                RecordType.FIRST -> {
                    FirstImpressionView(textState = textState)
                }

                RecordType.MIDDLE -> {
                    AddMiddleRecordView(textState = textState, pageState = pageState)
                }

                RecordType.FINAL -> {
                    ReadCompleteView(textState = textState)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddRecordScreenFirstPreview() {
    AppTheme {
        Column {
            AddRecordScreenUI(recordType = RecordType.FIRST)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddRecordScreenMiddlePreview() {
    AppTheme {
        Column {
            AddRecordScreenUI(recordType = RecordType.MIDDLE)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddRecordScreenFinalPreview() {
    AppTheme {
        Column {
            AddRecordScreenUI(recordType = RecordType.FINAL)
        }
    }
}