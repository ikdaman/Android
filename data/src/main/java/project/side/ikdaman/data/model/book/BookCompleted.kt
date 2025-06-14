package project.side.ikdaman.data.model.book

import project.side.ikdaman.core.utils.TimeUTC

data class BookCompleted(
    val review: String,
    val createdAt: String = TimeUTC.now()
)
