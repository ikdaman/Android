package project.side.ikdaman.core.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import project.side.ikdaman.core.ui.AppText
import project.side.ikdaman.core.ui.AppTheme


enum class CenterDialogType {
    DELETE_BOOK,
    DELETE_LOG
}


@Composable
fun CenterDialog(
    type: CenterDialogType,
    content: String? = "",
    dialogState: MutableTransitionState<Boolean> = remember { MutableTransitionState(true) },
    onDelete: () -> Unit = {},
) {
    AnimatedVisibility(
        visibleState = dialogState,
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        Dialog(
            onDismissRequest = {
                dialogState.targetState = false
            },
            properties = DialogProperties(
                dismissOnClickOutside = true,
                dismissOnBackPress = true
            )
        ) {
            if (type == CenterDialogType.DELETE_BOOK) {
                DeleteBookDialog(dialogState, onDelete)
            } else if (type == CenterDialogType.DELETE_LOG) {
                DeleteLogDialog(dialogState, content, onDelete = onDelete)
            }
        }
    }
}

@Composable
fun DeleteBookDialog(
    dialogState: MutableTransitionState<Boolean> = remember { MutableTransitionState(true) },
    onDelete: () -> Unit = {},
) {
    Column(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppText(
            "지금 이 책을 삭제하면\n책과 기록을 영영 복구하지 못해요 \uD83D\uDE22\n그래도 삭제하시겠어요?",
            style = DialogTitleStyle,
            maxLines = 3,
            modifier = Modifier.width(265.dp)
        )
        Spacer(Modifier.height(24.dp))
        Row {
            DialogButton(
                backgroundColor = Color.Black,
                text = "취소",
                onPressed = {
                    dialogState.targetState = false
                }
            )
            Spacer(Modifier.width(5.dp))
            DialogButton(
                backgroundColor = Color(0xFF858585),
                text = "삭제",
                onPressed = {
                    onDelete()
                    dialogState.targetState = false
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DeleteDialogPreview() {
    AppTheme {
        DeleteBookDialog()
    }
}


@Composable
fun DeleteLogDialog(
    dialogState: MutableTransitionState<Boolean> = remember { MutableTransitionState(true) },
    content: String? = "",
    onDelete: () -> Unit = {},
) {
    Column(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppText(
            "지금 이 기록을 삭제하면 \n" +
                    "책과 기록을 영영 복구하지 못해요 \uD83D\uDE22\n" +
                    "그래도 삭제하시겠어요?",
            style = DialogTitleStyle,
            maxLines = 3,
            modifier = Modifier.width(265.dp),
        )
        Spacer(Modifier.height(24.dp))
        Box(
            Modifier.sizeIn(maxHeight = 134.dp)
        ) {
            AppText(
                text = content,
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.W400,
                    letterSpacing = (-0.4).sp,
                    color = Color(0xFF333333),
                    textAlign = TextAlign.Start,
                ),
                maxLines = Int.MAX_VALUE,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFCFCFC))
                    .border(1.dp, Color(0xFFE2E2E2), RoundedCornerShape(10.dp))
                    .padding(horizontal = 20.dp, vertical = 17.dp)
                    .align(Alignment.Center)
                    .verticalScroll(rememberScrollState()),
            )
        }
        Spacer(Modifier.height(24.dp))
        Row {
            DialogButton(
                backgroundColor = Color.Black,
                text = "취소",
                onPressed = {
                    dialogState.targetState = false
                }
            )
            Spacer(Modifier.width(5.dp))
            DialogButton(
                backgroundColor = Color(0xFF858585),
                text = "삭제",
                onPressed = {
                    onDelete()
                    dialogState.targetState = false
                }
            )
        }
    }
}

val DialogTitleStyle = TextStyle(
    fontSize = 14.sp,
    lineHeight = 20.sp,
    fontWeight = FontWeight.SemiBold,
    letterSpacing = (-0.4).sp,
    color = Color.Black,
    textAlign = TextAlign.Center
)

@Preview(showBackground = true, widthDp = 393)
@Composable
fun DeleteLogDialogPreview() {
    AppTheme {
        DeleteLogDialog(
            dialogState = MutableTransitionState(true),
            content = "샘플",
            onDelete = {}
        )
    }
}