package project.side.ikdaman.data.model.responses

import project.side.ikdaman.domain.model.BookLog

data class BookLogResponse(
    val code: Int? = 0,
    val message: String? = "",
    val bookLog: BookLog? = null
) {
    fun isSuccess(): Boolean {
        return code == 0
    }
}
