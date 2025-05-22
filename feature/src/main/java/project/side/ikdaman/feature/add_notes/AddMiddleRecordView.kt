package project.side.ikdaman.feature.add_notes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import project.side.ikdaman.core.ui.AppText
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.utils.noEffectClick
import project.side.ikdaman.core.utils.oneClick
import project.side.ikdaman.domain.model.HomeBookItem

@Composable
fun AddMiddleRecordView(
    bookItem: HomeBookItem = HomeBookItem(),
    pageState: MutableState<Int?> = remember { mutableStateOf(null) },
    textState: MutableState<String> = remember { mutableStateOf("") },
    onConfirm: (String, Int) -> Unit = { _, _ -> },
    onNavigateToCompleteView: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight()
            .imePadding()
            .noEffectClick { focusManager.clearFocus() }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 30.dp)
        ) {
            AppText(
                "2024년 12월 23일 22시 15분의 기록 ✏\uFE0F",
                style = AddRecordTextStyles.titleTextStyle
            )
            Spacer(Modifier.height(10.dp))
            AppText(
                "소년이 온다 / 한강",
                style = AddRecordTextStyles.subtitleTextStyle
            )
            Spacer(Modifier.height(40.dp))
            Box(
                Modifier
                    .width(224.dp)
                    .height(71.dp)
            ) {
                AppText(
                    "어디까지 읽으셨나요?",
                    style = AddRecordTextStyles.middleTitleStyle,
                    modifier = Modifier.align(Alignment.TopStart)
                )
                Row(modifier = Modifier.align(Alignment.BottomStart)) {
                    BasicTextField(
                        value = pageState.value.toString().replace("null", "") + "p",
                        onValueChange = { newText ->
                            Log.i("AddMiddleRecordView", "onValueChange: $newText")
                            val number = newText.replace("null", "").replace("p", "").toIntOrNull()
                            if (number != null && number < bookItem.totalPage) {
                                pageState.value = number
                            }
                            if (number == null) {
                                pageState.value = null
                            }
                        },
                        modifier = Modifier
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(5.dp))
                            .padding(vertical = 12.dp)
                            .width(80.dp),
                        textStyle = AddRecordTextStyles.textFieldTextStyle.copy(
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                    Spacer(Modifier.width(5.dp))
                    AppText(
                        " / ${bookItem.totalPage}p",
                        style = AddRecordTextStyles.textFieldTextStyle.copy(
                            color = Color(0xFFA6A6A6),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(5.dp))
                            .padding(vertical = 12.dp)
                            .width(80.dp),
                    )
                }

                Surface(
                    shadowElevation = 4.dp,
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .align(Alignment.TopEnd)
                        .oneClick {
                            onNavigateToCompleteView()
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .padding(vertical = 7.dp, horizontal = 10.dp)
                    ) {
                        AppText(
                            "✌다 읽었어요!",
                            style = AddRecordTextStyles.commentTextStyle.copy(color = Color.Black),
                        )
                    }
                }
            }

            Spacer(Modifier.height(50.dp))

            AppText("독서하며 든 생각", style = AddRecordTextStyles.middleTitleStyle)
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF5F5F5))
                    .fillMaxWidth()
                    .defaultMinSize(
                        minHeight = 169.dp
                    )
                    .padding(16.dp)
            ) {
                // 배경이 없는 TextField
                if (textState.value.isEmpty()) {
                    AppText(
                        "독서 중 떠오르는 생각을 마음 가는대로 적어보세요.",
                        style = AddRecordTextStyles.textFieldHintStyle,
                        maxLines = 2
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    BasicTextField(
                        value = textState.value,
                        onValueChange = {
                            if (it.length <= 500) {
                                textState.value = it
                            }
                        },
                        textStyle = AddRecordTextStyles.textFieldTextStyle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 100.dp)
                    )
                    val currentTextLength = textState.value.length
                    Spacer(Modifier.height(15.dp))
                    AppText(
                        "$currentTextLength/500",
                        style = AddRecordTextStyles.textFieldTextStyle,
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Black)
                .fillMaxWidth()
                .height(50.dp)
                .oneClick(500) {
                    if (textState.value.isNotEmpty() && pageState.value != null) {
                        onConfirm(textState.value, pageState.value!!)
                    }
                }
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
}

@Preview(showBackground = true, widthDp = 393, heightDp = 800)
@Composable
fun AddMiddleRecordViewPreview() {
    AppTheme {
        AddMiddleRecordView(
            bookItem = HomeBookItem(
                id = "0",
                imageUrl = "https://picsum.photos/250/284?random=1",
                addedDateTime = System.currentTimeMillis(),
                lastEditedDateTime = System.currentTimeMillis(),
                title = "소년이 온다1",
                author = "한강1",
                firstImpression = "테스트 테스트",
                progress = 1f,
                totalPage = 260
            ),
            pageState = remember { mutableStateOf(null) },
        )
    }
}