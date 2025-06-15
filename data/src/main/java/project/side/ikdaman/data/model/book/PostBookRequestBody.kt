package project.side.ikdaman.data.model.book

data class PostBookRequestBody(
    val title: String,
    val writer: String,
    val publisher: String,
    val isbn: String,
    val page: Int,
    val coverImage: String,
    val impression: String,
    val createdAt: String
)
