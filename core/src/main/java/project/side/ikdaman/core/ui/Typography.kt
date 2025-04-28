package project.side.ikdaman.core.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import project.side.ikdaman.app.core.R

val PretendardFontFamily = FontFamily(
    Font(R.font.pretendard_bold, weight = FontWeight.Bold),
    Font(R.font.pretendard_semibold, weight = FontWeight.SemiBold),
    Font(R.font.pretendard_regular, weight = FontWeight.Normal),
)

@Composable
fun AppText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle(),
    maxLines: Int = 1,
    softWrap: Boolean = true,
) {
    Text(
        modifier = modifier,
        text = text,
        style = style.copy(fontFamily = PretendardFontFamily),
        maxLines = maxLines,
        softWrap = softWrap,
    )
}
