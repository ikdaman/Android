package project.side.ikdaman.domain.repository

import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.BookSearchEntity

interface BookRepository {
    suspend fun searchBookWithTitle(
        title: String,
        startPage: Int
    ): ApiResult<BookSearchEntity>

    suspend fun searchBookWithIsbn(isbn: String): ApiResult<BookSearchEntity>
}