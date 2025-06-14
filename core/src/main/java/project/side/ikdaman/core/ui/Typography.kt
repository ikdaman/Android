package project.side.ikdaman.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import project.side.ikdaman.app.core.R

val PretendardFontFamily = FontFamily(
    Font(R.font.pretendard_bold, weight = FontWeight.Bold),
    Font(R.font.pretendard_semibold, weight = FontWeight.SemiBold),
    Font(R.font.pretendard_regular, weight = FontWeight.Normal),
)

@Composable
fun AppText(
    text: String?,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle(),
    maxLines: Int = 1,
    softWrap: Boolean = true,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    height: Dp? = null
) {
    if (height != null) {
        Box(Modifier.height(height)) {
            Text(
                modifier = modifier.align(Alignment.Center),
                text = text ?: "",
                style = style.copy(fontFamily = PretendardFontFamily),
                maxLines = maxLines,
                softWrap = softWrap,
                overflow = overflow,
            )
        }
    } else {
        Text(
            modifier = modifier,
            text = text ?: "",
            style = style.copy(fontFamily = PretendardFontFamily),
            maxLines = maxLines,
            softWrap = softWrap,
            overflow = overflow,
        )
    }
}
