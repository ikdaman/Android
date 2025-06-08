package project.side.ikdaman.feature.detail

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object DetailScreenTextStyle {
    val appBarTitle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        letterSpacing = (-0.4).sp,
        color = Color.Black
    )

    val bookTitle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        letterSpacing = (-0.4).sp,
        lineHeight = 19.sp,
        color = Color.Black
    )

    val bookInfoKey = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        letterSpacing = (-0.4).sp,
        lineHeight = 17.sp,
        color = Color.Black
    )

    val bookInfoValue = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = (-0.4).sp,
        lineHeight = 17.sp,
        color = Color(0xFF666666)
    )

    val aladinTextKey = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = (-0.4).sp,
        color = Color.Black
    )

    val aladinTextValue = aladinTextKey.copy(fontWeight = FontWeight.Bold)
}