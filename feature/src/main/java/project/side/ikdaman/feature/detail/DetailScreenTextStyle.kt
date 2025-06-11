package project.side.ikdaman.feature.detail

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object DetailScreenTextStyle {
    val addRecord: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        letterSpacing = (-0.4).sp,
        lineHeight = 28.sp,
        color = Color.White
    )
    val impressionText: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        letterSpacing = (-0.4).sp,
        lineHeight = 18.sp,
        color = Color(0xFF666666)
    )
    val subTitle: TextStyle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        letterSpacing = (-0.4).sp,
        lineHeight = 19.sp,
        color = Color.Black
    )
    val progressTextBold: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        letterSpacing = (-0.4).sp,
        lineHeight = 24.sp,
        color = Color.Black
    )

    val progressTextNormal: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        letterSpacing = (-0.4).sp,
        lineHeight = 24.sp,
        color = Color.Black
    )

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

    val readDone = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = (-0.4).sp,
        color = Color.Black
    )
}