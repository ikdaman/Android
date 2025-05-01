package project.side.ikdaman.data.model.book

import project.side.ikdaman.domain.model.HomeBookItem

data class HomeBook(
    val mybookId: Int,
    val title: String = "",
    val author: String = "",
    val progress: Int = 0,
    val coverImage: String = "",
    val firstImpression: String = "",
    val recentEdit: String // yyyy-MM-ddThh:mm:ssZ format
) {
    fun transformToDomain(): HomeBookItem {
        // yyyy-MM-ddThh:mm:ssZ 형태의 문자열을 Long으로 변환
        val lastEditedTime = try {
            recentEdit.split("T").first().split("-").let {
                it[0].toLong() * 1000 * 60 * 60 * 24 * 365 +
                        it[1].toLong() * 1000 * 60 * 60 * 24 * 30 +
                        it[2].toLong() * 1000 * 60 * 60 * 24
            }
        } catch (e: Exception) {
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