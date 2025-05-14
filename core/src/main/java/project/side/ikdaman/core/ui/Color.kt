package project.side.ikdaman.core.ui

import androidx.compose.ui.graphics.Color

object Palette {
    fun getColor(color: Long): Color {
        return when (color) {
            0xFFC1A4DB -> first
            0xFF4C8ED9 -> second
            0xFF26843B -> third
            0xFF694E4E -> fourth
            0xFF595959 -> fifth
            else -> first
        }
    }

    fun fromColor(color: Color): Long {
        return when (color) {
            first -> 0xFFC1A4DB
            second -> 0xFF4C8ED9
            third -> 0xFF26843B
            fourth -> 0xFF694E4E
            fifth -> 0xFF595959
            else -> 0xFFC1A4DB
        }
    }

    val first = Color(0xFFC1A4DB)
    val second = Color(0xFF4C8ED9)
    val third = Color(0xFF26843B)
    val fourth = Color(0xFF694E4E)
    val fifth = Color(0xFF595959)

    val paletteColors = listOf(
        first,
        second,
        third,
        fourth,
        fifth
    )
}