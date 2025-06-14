package project.side.ikdaman.domain.model

import java.text.SimpleDateFormat
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
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
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
    val itemId: String? = "306367260", // TODO 서버에서 아직 작업 안됨
)
