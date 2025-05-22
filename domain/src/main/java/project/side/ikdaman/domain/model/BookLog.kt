package project.side.ikdaman.domain.model

import java.text.SimpleDateFormat
import java.util.Locale

data class BookLog(
    val booklogs: List<BookLogItem>,
    val hasNext: Boolean,
)

data class BookLogItem(
    val booklogId: Int,
    val loggedDate: String,
    val page: Int,
    val content: String,
    val type: String
) {
    fun getDateByLong(): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        return dateFormat.parse(loggedDate)?.time ?: System.currentTimeMillis()
    }
}
