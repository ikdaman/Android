package project.side.ikdaman.domain.model

data class BookShelfBooks(
    val books: List<BookShelfItem> = emptyList(),
    val totalPage: Int = 0,
    val nowPage: Int = 0
)

data class BookShelfItem(
    val mybookId: Long = 0L,
    val title: String = "",
    val author: String = "",
    val coverImage: String = "",
    val isCompleted: Boolean = false,
)
