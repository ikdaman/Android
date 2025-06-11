package project.side.ikdaman.domain.model

import java.util.Locale

data class BookDetail(
    val bookInfo: BookInfo = BookInfo(),
    val mybookId: String = "",
    val startDate: String = "",
    val nowPage: Int = 0,
    val progress: Int = 0,
    val impression: String? = "",
    val code: Int? = 0,
    val message: String? = "",
) {
    fun isSuccess(): Boolean {
        return code == 0
    }

    fun progressText(): String {
        val now = System.currentTimeMillis()
        val startTime = try {
            val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            dateFormat.parse(startDate)?.time ?: now
        } catch (_: Exception) {
            now // 변환 실패 시 현재 시간으로 설정
        }
        val elapsedDays = (now - startTime) / (1000 * 60 * 60 * 24)
        return "${elapsedDays}일째, ${nowPage}p, ${progress}%"
    }
}

data class BookInfo(
    val title: String = "",
    val author: String = "",
    val coverImage: String = "",
    val publisher: String = "",
    val totalPage: Int = 0,
)
