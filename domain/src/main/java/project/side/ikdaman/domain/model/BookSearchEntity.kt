package project.side.ikdaman.domain.model

data class BookSearchEntity(
    val totalBookCount: Int = 0,
    val books: List<BookSearchItemEntity> = emptyList()
)

data class BookSearchItemEntity(
    val title: String = "",
    val author: String = "",
    val cover: String = "",
    val isbn: String = "",
    val itemId: Long = 0L,
    val link: String = "",
    val publisher: String = "",
    val subInfo: BookSubInfo? = null
)

data class BookSubInfo(
    val itemPage: String? = null
)
