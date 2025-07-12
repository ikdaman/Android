package project.side.ikdaman.data.model.responses

import project.side.ikdaman.domain.model.BookShelfBooks
import project.side.ikdaman.domain.model.BookShelfItem

data class BookShelfResponse(
    val books: List<BookShelfItem>,
    val totalPage: Int = 0,
    val nowPage: Int = 0,
    val code: Int = 0,
    val message: String = ""
)

fun BookShelfResponse.toDomain() = BookShelfBooks(
    books = books,
    totalPage = totalPage,
    nowPage = nowPage
)

