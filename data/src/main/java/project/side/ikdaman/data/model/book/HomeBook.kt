package project.side.ikdaman.data.model.book

import project.side.ikdaman.domain.model.HomeBookItem
import java.text.SimpleDateFormat
import java.util.Locale

data class HomeBook(
    val mybookId: Int,
    val title: String = "",
    val author: String = "",
    val progress: Int = 0,
    val coverImage: String = "",
    val firstImpression: String = "",
    val recentEdit: String // yyyy-MM-ddTHH:mm:ssZ format
) {
    fun transformToDomain(): HomeBookItem {
        val lastEditedTime = try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            dateFormat.parse(recentEdit)?.time ?: System.currentTimeMillis()
        } catch (_: Exception) {
            System.currentTimeMillis() // 변환 실패 시 현재 시간으로 설정
        }

        return HomeBookItem(
            id = mybookId.toString(),
            imageUrl = coverImage,
            lastEditedTime = lastEditedTime,
            title = title,
            author = author,
            progress = progress.toFloat() / 100f,
            firstImpression = firstImpression
        )
    }
}