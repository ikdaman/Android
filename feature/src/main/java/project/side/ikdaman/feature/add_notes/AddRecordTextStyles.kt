package project.side.ikdaman.feature.add_notes

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

object AddRecordTextStyles {
    val appBarTitleTextStyle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        letterSpacing = (-0.4).sp,
        lineHeight = 21.sp,
        textAlign = TextAlign.Center
    )
    val titleTextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        letterSpacing = (-0.4).sp,
        lineHeight = 26.sp,
    )
    val subtitleTextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = (-0.4).sp,
        lineHeight = 14.sp,
    )
    val middleTitleStyle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        letterSpacing = (-0.4).sp,
        lineHeight = 17.sp,
        color = Color(0xFF000000)
    )
    val textFieldHintStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = (-0.4).sp,
        lineHeight = 18.sp,
        color = Color(0xFF626262),
    )
    val textFieldTextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = (-0.4).sp,
        lineHeight = 18.sp,
        color = Color(0xFF333333)
    )
    val commentTextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = (-0.4).sp,
        lineHeight = 16.sp,
        color = Color(0xFF888888)
    )
    val buttonTextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        letterSpacing = (-0.4).sp,
        lineHeight = 28.sp,
        color = Color(0xFFFFFFFF),
        textAlign = TextAlign.Center
    )
}