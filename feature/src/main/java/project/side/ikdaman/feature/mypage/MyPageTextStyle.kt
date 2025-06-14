package project.side.ikdaman.feature.mypage

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import project.side.ikdaman.core.ui.PretendardFontFamily

object MyPageTextStyle {
    val TitleText = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        color = Color.Black,
        letterSpacing = (-0.4).sp,
        fontFamily = PretendardFontFamily,
    )

    val MenuText = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        color = Color.Black,
        letterSpacing = (-0.4).sp,
        fontFamily = PretendardFontFamily,
    )

    val SubMenuText = MenuText.copy(fontSize = 16.sp)

    val LabelText = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = Color.Black,
        letterSpacing = (-0.4).sp,
        fontFamily = PretendardFontFamily
    )

    val ButtonText = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = Color.White,
        letterSpacing = (-0.4).sp,
        fontFamily = PretendardFontFamily
    )

    val CheckButtonText = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = Color.White,
        letterSpacing = (-0.4).sp,
        fontFamily = PretendardFontFamily
    )

    val GenderButtonText = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = Color(0xFFA6A6A6),
        letterSpacing = (-0.4).sp,
        fontFamily = PretendardFontFamily
    )

    val TextFieldText = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = (-0.4).sp,
        fontFamily = PretendardFontFamily
    )

    fun TextStyle.withFontSize(fontSize: TextUnit): TextStyle {
        return this.copy(fontSize = fontSize)
    }
}