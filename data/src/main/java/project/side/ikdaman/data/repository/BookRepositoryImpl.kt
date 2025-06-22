package project.side.ikdaman.data.repository

import android.util.Log
import project.side.ikdaman.data.data_source.BookItemModelMapper.toDomain
import project.side.ikdaman.data.service.BookService
import project.side.ikdaman.domain.model.BookSearchResult
import project.side.ikdaman.domain.repository.BookRepository
import javax.inject.Inject

val TAG = "BookRepositoryImpl"

class BookRepositoryImpl @Inject constructor(private val bookService: BookService): BookRepository {
    override suspend fun searchBookWithTitle(title: String): BookSearchResult {
        try {
            val response = bookService.searchBookWithTitle(query = title)
            return response.toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "searchBookWithTitle: $e")
            return BookSearchResult()
        }
    }

    override suspend fun searchBookWithIsbn(isbn: String): BookSearchResult {
        try {
            val response = bookService.searchBookWithIsbn(itemId = isbn)
            return response.toDomain()
        } catch (e: Exception) {
            return BookSearchResult()
        }
    }
}