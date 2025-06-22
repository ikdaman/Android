package project.side.ikdaman.data.model.book

import project.side.ikdaman.domain.model.HomeBookItem
import project.side.ikdaman.domain.util.convertUtcToLocalLong
import java.text.SimpleDateFormat
import java.util.Locale

data class HomeBook(
    val mybookId: Int,
    val title: String = "",
    val author: String = "",
    val progress: String = "0",
    val coverImage: String = "",
    val firstImpression: String? = "",
    val recentEdit: String
) {
    fun transformToDomain(): HomeBookItem {
        val lastEditedTime = try {
            convertUtcToLocalLong(recentEdit)
        } catch (_: Exception) {
            System.currentTimeMillis() // 변환 실패 시 현재 시간으로 설정
        }

        return HomeBookItem(
            id = mybookId.toString(),
            imageUrl = coverImage,
            lastEditedDateTime = lastEditedTime,
            title = title,
            author = author,
            progress = progress.toFloat() / 100f,
            firstImpression = firstImpression ?: "",
        )
    }
}