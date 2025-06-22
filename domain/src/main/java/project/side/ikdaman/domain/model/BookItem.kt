package project.side.ikdaman.domain.model

data class BookSearch(
    val totalBookCount: Int = 0,
    val books: List<BookItem> = emptyList()
)

data class BookItem(
    val title: String,
    val author: String,
    val cover: String,
    val isbn: String,
    val itemId: Long,
    val publisher: String,
    val subInfo: BookSubInfo? = null
)

data class BookSubInfo(
    val itemPage: String
)
