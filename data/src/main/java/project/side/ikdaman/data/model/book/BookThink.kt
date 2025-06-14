package project.side.ikdaman.data.model.book

import project.side.ikdaman.core.utils.TimeUTC

data class BookThink(
    val content: String,
    val page: Int,
    val createdAt: String = TimeUTC.now()
)

data class UpdateBookThink(
    val content: String
)