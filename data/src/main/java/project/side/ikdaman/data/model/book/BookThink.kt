package project.side.ikdaman.data.model.book

import project.side.ikdaman.data.utils.TimeUtils

data class BookThink(
    val content: String,
    val page: Int,
    val createdAt: String,
) {
    companion object {
        fun create(content: String, page: Int): BookThink {
            return BookThink(
                content = content,
                page = page,
                createdAt = TimeUtils.nowUTC()
            )
        }
    }
}

data class UpdateBookThink(
    val content: String
)