package project.side.ikdaman.core.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun DeleteDialog(
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
            Column(
                Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppText(
                    "지금 이 책을 삭제하면\n책과 기록을 영영 복구하지 못해요 \uD83D\uDE22\n그래도 삭제하시겠어요?",
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-0.4).sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    ),
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
    }
}

@Composable
@Preview(showBackground = true)
fun DeleteDialogPreview() {
    AppTheme {
        DeleteDialog()
    }
}