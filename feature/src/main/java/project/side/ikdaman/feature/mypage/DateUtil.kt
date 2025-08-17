package project.side.ikdaman.feature.mypage

import androidx.compose.ui.text.input.TextFieldValue
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle

fun isValidDate(dateStr: String): Boolean {
    return try {
        val date = LocalDate.parse(
            dateStr, DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(
                ResolverStyle.STRICT
            )
        )
        date <= LocalDate.now()
    } catch (e: Exception) {
        false
    }
}

private fun calculateCursorPosition(    //yyyy-MM-dd 형식 커서 위치 계산 함수
    oldText: String,
    newText: String,
    newCursorRawPos: Int
): Int {
    val dashPositions = setOf(4, 7)
    var cursor = newCursorRawPos

    val isInsert = newText.length > oldText.length
    val isDelete = newText.length < oldText.length

    when {
        isInsert && dashPositions.contains(cursor - 1) -> cursor += 1
        isDelete && dashPositions.contains(cursor) -> cursor -= 1
        !isInsert && !isDelete && dashPositions.contains(cursor) -> cursor += 1
    }

    return cursor.coerceIn(0, newText.length)
}

fun onDateChanged(      // yyyy-MM-dd 하이픈 자동 추가, 삭제
    oldValue: TextFieldValue,
    newValue: TextFieldValue
): Pair<String, Int> {
    val oldDigits = oldValue.text.filter { it.isDigit() }
    val newDigits = newValue.text.filter { it.isDigit() }

    val isDelete = newDigits.length < oldDigits.length
    val digits = if (isDelete) oldDigits.dropLast(1) else newDigits.take(8)

    val formatted = buildString {
        digits.forEachIndexed { i, c ->
            if (i == 4 || i == 6) append("-")
            append(c)
        }
    }.take(10)
    val correctedCursor = calculateCursorPosition(oldValue.text, formatted, newValue.selection.end)

    return formatted to correctedCursor
}