package project.side.ikdaman.data.model

import project.side.ikdaman.domain.model.BookSearchItemEntity
import project.side.ikdaman.domain.model.BookSearchEntity
import project.side.ikdaman.domain.model.BookSubInfo

data class BookSearchDto(
    val totalResults: Int,
    val item: List<BookSearchItem>
) : BaseDto<BookSearchEntity> {

    override fun toEntity(): BookSearchEntity {
        return BookSearchEntity(
            totalBookCount = totalResults,
            books = item.map { it.toEntity() }
        )
    }
}

data class BookSearchItem(
    val title: String,
    val link: String,
    val author: String,
    val cover: String,
    val publisher: String,
    val isbn: String?,
    val isbn13: String?,
    val itemId: Long,
    val subInfo: BookSubInfoResponse? = null
) {
    fun toEntity() = BookSearchItemEntity(
        title = title,
        author = author,
        cover = cover,
        publisher = publisher,
        isbn = isbn13 ?: isbn ?: "",
        itemId = itemId,
        link = link,
        subInfo = subInfo.toEntity()
    )

    private fun BookSubInfoResponse?.toEntity() =
        if (this == null) null else BookSubInfo(itemPage = itemPage)
}

data class BookSubInfoResponse(
    val itemPage: String? = null
)