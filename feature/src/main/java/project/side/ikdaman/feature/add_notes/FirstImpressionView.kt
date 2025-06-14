package project.side.ikdaman.feature.add_notes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.BookDetail
import project.side.ikdaman.domain.model.BookInfo

@Composable
fun FirstImpressionView(
    result: ApiResult<BookDetail>,
    textState: MutableState<String> = remember { mutableStateOf("") },
    onConfirm: (String) -> Unit = {},
) {
    if (result is ApiResult.Error) {
        Log.e("AddMiddleRecordView", "Error loading book item: ${result.message}")
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            AppText(
                "책 정보를 불러오는 데 실패했습니다.\n${result.message}",
                modifier = Modifier.align(Alignment.Center),
            )
        }
        return
    }
    if (result is ApiResult.Loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            AppText("책 정보를 불러오는 중...")
        }
        return
    }
    val data = (result as ApiResult.Success<BookDetail>).data

    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.White)
            .imePadding()
            .noEffectClick { focusManager.clearFocus() }
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 30.dp)
        ) {
            AppText("이 책의 첫인상", style = AddRecordTextStyles.titleTextStyle)
            Spacer(Modifier.height(10.dp))
            AppText("${data.bookInfo.title} / ${data.bookInfo.author}", style = AddRecordTextStyles.subtitleTextStyle)
            Spacer(Modifier.height(40.dp))
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
                        "처음 책을 보고 들었던 생각을 짧게 적어보세요.\n" +
                                "독서가 마음처럼 잘되지 않을 때, 나에게 힘을 줄 거예요!",
                        style = AddRecordTextStyles.textFieldHintStyle,
                        maxLines = 2
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
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
            Spacer(Modifier.height(17.dp))
            AppText(
                "* 첫인상은 추후 수정과 삭제가 어려워요. \n" +
                        "   나의 첫 생각을 간직하기 위함이니 참고해주세요.  ☺\uFE0F",
                style = AddRecordTextStyles.commentTextStyle,
                maxLines = 2
            )
        }

        Box(
            modifier = Modifier
                .padding(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Black)
                .fillMaxWidth()
                .height(50.dp)
                .oneClick(500) {
                    if (textState.value.isNotEmpty()) {
                        onConfirm(textState.value)
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

@Preview(showBackground = true, heightDp = 600)
@Composable
fun FirstImpressionViewPreview() {
    AppTheme {
        FirstImpressionView(
            result = ApiResult.Success(
                BookDetail(
                    bookInfo = BookInfo(
                        title = "책 제목",
                        author = "저자 이름",
                        totalPage = 300,
                    )
                )
            ),
        )
    }
}