package project.side.ikdaman.data.model.responses

import project.side.ikdaman.domain.model.BookDetail

data class BookDetailResponse(
    val code: Int? = 0,
    val message: String? = "",
    val book: BookDetail? = null
) {
    fun isSuccess(): Boolean {
        return code == 0
    }
}

