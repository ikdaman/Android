package project.side.ikdaman.domain.model

data class BookDetail(
    val bookInfo: BookInfo,
    val mybookId: String,
    val startDate: String,
    val nowPage: Int,
    val progress: Int,
    val impression: String
)

data class BookInfo(
    val title: String,
    val author: String,
    val coverImage: String,
    val publisher: String,
    val totalPage: Int
)
