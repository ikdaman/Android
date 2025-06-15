package project.side.ikdaman.data.model.book

data class BookThink(
    val content: String,
    val page: Int
)

data class UpdateBookThink(
    val content: String
)

data class UpdateBookCompleted(
    val content: String
)