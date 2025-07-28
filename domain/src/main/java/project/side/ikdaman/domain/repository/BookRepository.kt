package project.side.ikdaman.domain.repository

import project.side.ikdaman.domain.model.BookSearchResult

interface BookRepository {
    suspend fun searchBookWithTitle(
        title: String,
        startPage: Int
    ): BookSearchResult

    suspend fun searchBookWithIsbn(isbn: String): BookSearchResult
}