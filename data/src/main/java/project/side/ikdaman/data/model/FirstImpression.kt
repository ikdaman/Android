package project.side.ikdaman.data.model

import project.side.ikdaman.core.utils.TimeUTC

data class FirstImpression(
    val impression: String,
    val createdAt: String = TimeUTC.now()
)
