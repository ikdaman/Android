package project.side.ikdaman.feature.bookshelf

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import project.side.ikdaman.core.ui.PretendardFontFamily

object BookShelfTextStyle {
    val FilterText = TextStyle(
        color = Color.White,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = (-0.4).sp,
        fontFamily = PretendardFontFamily
    )

    val emptyBookShelfText = TextStyle(
        color = Color.Black,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        letterSpacing = (-0.4).sp,
        fontFamily = PretendardFontFamily
    )
}