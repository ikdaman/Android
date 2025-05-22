package project.side.ikdaman.data.model

import project.side.ikdaman.data.utils.TimeUtils

data class FirstImpression(
    val impression: String,
    val createdAt: String
) {
    companion object {
        fun create(impression: String): FirstImpression {
            return FirstImpression(
                impression = impression,
                createdAt = TimeUtils.nowUTC()
            )
        }
    }
}
