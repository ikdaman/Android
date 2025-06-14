package project.side.ikdaman.domain.model

import java.text.SimpleDateFormat
import java.util.Locale

data class BookLog(
    val booklogs: List<BookLogItem>,
    val hasNext: Boolean,
    val code: Int? = 0,
    val message: String? = "",
) {
    fun isSuccess(): Boolean {
        return code == 0
    }
}

data class BookLogItem(
    val booklogId: Int,
    val loggedDate: String,
    val page: Int,
    val content: String?,
    val type: String
) {
    fun getDateByLong(): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
        return dateFormat.parse(loggedDate)?.time ?: System.currentTimeMillis()
    }

    fun getLogString(): String {
        // yy-MM-dd HH:mm 형식
        val dateTime = getDateByLong()
        val dateFormat = SimpleDateFormat("yy/MM/dd HH:mm", Locale.getDefault())
        return dateFormat.format(dateTime)
    }

    fun getLogTypeText(): String {
        val typeText = when (type) {
            "THINK" -> "✏\uFE0F 생각을 추가했어요."
            "IMPRESSION" -> "\uD83D\uDC95 첫인상을 추가했어요."
            "REVIEW" -> "\uD83C\uDFB5 책을 덮었어요."
            else -> "\uD83D\uDCD6  책을 펼쳤어요."
        }
        return typeText
    }

    fun isNotOpenLog(): Boolean {
        return type != "OPEN"
    }

    fun hasPage(): Boolean {
        return type == "THINK"
    }

    fun isNotImpression(): Boolean {
        return type != "IMPRESSION"
    }

}
