package project.side.ikdaman.domain.model

data class NoticeItem(
    val notices: List<Notice>,
    val hasNext: Boolean,
    val currentPage: Int,
    val totalPage: Int
)

data class Notice(
    val noticeId: Long,
    val title: String,
    val uploadedAt: String
)

data class NoticeDetail(
    val noticeId: Long,
    val title: String,
    val content: String,
    val uploadedAt: String,
    val noticeWriter: String
)