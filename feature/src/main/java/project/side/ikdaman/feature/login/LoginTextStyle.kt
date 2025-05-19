package project.side.ikdaman.feature.login

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import project.side.ikdaman.core.ui.PretendardFontFamily

object LoginTextStyle {
    val MainDescText = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        color = Color.Black,
        letterSpacing = (-0.4).sp,
        fontFamily = PretendardFontFamily
    )

    val SubDescText = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.Black,
        letterSpacing = (-0.4).sp,
        fontFamily = PretendardFontFamily
    )

    val TermsRegularText = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = Color.Black,
        letterSpacing = (-0.32).sp,
        fontFamily = PretendardFontFamily
    )

    val TermsBoldText = TermsRegularText.copy(
        fontWeight = FontWeight.Bold
    )

    val LoginButtonText = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 19.sp,
        letterSpacing = (-0.4).sp,
        color = Color.Black,
        fontFamily = PretendardFontFamily
    )
}