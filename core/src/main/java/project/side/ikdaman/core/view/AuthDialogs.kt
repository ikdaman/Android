package project.side.ikdaman.core.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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

@Composable
fun AppDialog(
    title: String = "",
    visible: Boolean = true,
    enabled: Boolean = true,
    cancelButtonText: String = "아니요",
    confirmButtonText: String = "네",
    onDismissRequest: () -> Unit = {},
    onConfirmClicked: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                dismissOnClickOutside = true,
                dismissOnBackPress = true
            )
        ) {
            Column(
                modifier = Modifier
                    .width(305.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = title, style = DialogTitleStyle)
                content()
                Spacer(modifier = Modifier.height(24.dp))
                Row {
                    AppDialogButton(
                        text = cancelButtonText,
                        onClick = onDismissRequest
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    AppDialogButton(
                        enabled = enabled,
                        text = confirmButtonText,
                        backgroundColor = Color(0xFF858585),
                        onClick = onConfirmClicked
                    )
                }
            }
        }
    }
}

@Composable
fun AppDialogButton(
    enabled: Boolean = true,
    text: String = "",
    backgroundColor: Color = Color.Black,
    textColor: Color = Color.White,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = Modifier.height(30.dp),
        onClick = onClick,
        enabled = enabled,
        contentPadding = PaddingValues(horizontal = 0.dp),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
    ) {
        Row {
            Spacer(modifier = Modifier.width(30.dp))
            Text(
                text = text,
                style = TextStyle(
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = (-0.4).sp
                )
            )
            Spacer(modifier = Modifier.width(30.dp))
        }
    }
}

@Composable
fun WithdrawDialog(
    showDialog: Boolean = true,
    onConfirmClicked: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    val isChecked = remember { mutableStateOf(false) }
    AppDialog(
        title = "탈퇴 후 아래 기록과 계정 복구가 불가능해요.\n" +
                "그래도 탈퇴하시겠어요?\n",
        visible = showDialog,
        enabled = isChecked.value,
        onConfirmClicked = onConfirmClicked,
        onDismissRequest = onDismissRequest,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "∙ 독서중인 책, 다 읽은 책\n" +
                        "∙ 책의 첫인상, 생각 기록\n" +
                        "∙ 내 책장",
                style = TextStyle(
                    color = Color(0xFF696969),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = (-0.4).sp
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isChecked.value,
                    onCheckedChange = { isChecked.value = !isChecked.value },
                    modifier = Modifier.size(18.dp),
//                    colors = CheckboxDefaults.colors()
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "네, 탈퇴할게요",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-0.4).sp
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun AppDialogPreview() {
    AppDialog("내용") {}
}

@Preview
@Composable
fun WithdrawDialogPreview() {
    WithdrawDialog()
}