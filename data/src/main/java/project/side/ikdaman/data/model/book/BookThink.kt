package project.side.ikdaman.data.model.book

import project.side.ikdaman.core.utils.TimeUTC

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
                createdAt = TimeUTC.now()
            )
        }
    }

    fun toJsonString(): String {
        return """{"content":"$content","page":$page,"createdAt":"$createdAt"}"""
    }
}

data class UpdateBookThink(
    val content: String
)