package project.side.ikdaman.domain.model

data class BookDetail(
    val bookInfo: BookInfo = BookInfo(),
    val mybookId: String = "",
    val startDate: String = "",
    val nowPage: Int = 0,
    val progress: Int = 0,
    val impression: String = "",
    val code: Int? = 0,
    val message: String? = "",
) {
    fun isSuccess(): Boolean {
        return code == 0
    }
}

data class BookInfo(
    val title: String = "",
    val author: String = "",
    val coverImage: String = "",
    val publisher: String = "",
    val totalPage: Int = 0,
)
