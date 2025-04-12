package project.side.ikdaman.feature.home

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object HomeTextStyles {
    val bubbleTextBold = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 17.sp,
        letterSpacing = (-0.4).sp
    )
    val bubbleTextRegular = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 17.sp,
        letterSpacing = (-0.4).sp,
        color = Color(0xFF727272)
    )
    val bookTitleText = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 21.sp,
        letterSpacing = (-0.4).sp
    )
    val bookAuthorText = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 21.sp,
        letterSpacing = (-0.4).sp
    )
    val buttonText = bookAuthorText.copy(lineHeight = 19.sp)
    val bottomTitle = bubbleTextBold.copy(lineHeight = 21.sp)
    val bottomDescription = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.4).sp,
        color = Color(0xFF666666)
    )
}