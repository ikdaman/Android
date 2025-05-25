package project.side.ikdaman.core.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.side.ikdaman.core.ui.AppText
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.utils.oneClick

@Composable
fun DialogButton(
    backgroundColor: Color,
    textColor: Color = Color.White,
    text: String,
    onPressed: () -> Unit,
) {
    Box(
        modifier = Modifier
            .height(30.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(backgroundColor)
            .padding(vertical = 5.dp, horizontal = 30.dp)
            .oneClick { onPressed() }
    ) {
        AppText(
            text = text,
            style = TextStyle(
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = (-0.4).sp
            ),
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DialogButtonPreview() {
    AppTheme {
        Column {
            DialogButton(
                backgroundColor = Color.Black,
                text = "취소",
                onPressed = {}
            )
            DialogButton(
                backgroundColor = Color(0xFF858585),
                text = "삭제",
                onPressed = {}
            )
        }
    }
}