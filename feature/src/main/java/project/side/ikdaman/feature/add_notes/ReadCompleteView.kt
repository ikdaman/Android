package project.side.ikdaman.feature.add_notes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import project.side.ikdaman.core.ui.AppText
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.utils.noEffectClick
import project.side.ikdaman.core.utils.oneClick

@Composable
fun ReadCompleteView(
    modifier: Modifier = Modifier,
    textState: MutableState<String> = remember { mutableStateOf("") },
    onConfirm: (String) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxHeight()
            .background(Color.White)
            .imePadding()
            .verticalScroll(rememberScrollState(), reverseScrolling = true)
            .noEffectClick { focusManager.clearFocus() }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 30.dp),
        ) {
            AppText(
                "완독을 축하드려요! \uD83E\uDD73",
                style = AddRecordTextStyles.titleTextStyle
            )
            Spacer(Modifier.height(30.dp))
            AppText(
                "완독 후의 생각",
                style = AddRecordTextStyles.middleTitleStyle
            )
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
                        "책을 다 읽고 나서 느낀 점을 적어보세요.",
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
                    onConfirm(textState.value)
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

@Preview(showBackground = true, heightDp = 800)
@Composable
fun ReadCompleteViewPreview() {
    AppTheme {
        ReadCompleteView()
    }
}