package project.side.ikdaman.data.model.responses

import project.side.ikdaman.data.model.book.HomeBook

data class BooksResponse(
    val books: List<HomeBook>?,
    val code: Int? = 0,
    val message: String? = ""
) {
    fun isSuccess(): Boolean {
        return code == 0
    }
}