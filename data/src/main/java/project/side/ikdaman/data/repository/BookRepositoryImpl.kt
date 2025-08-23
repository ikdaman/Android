package project.side.ikdaman.data.repository

import android.util.Log
import project.side.ikdaman.data.data_source.remote.BookService
import project.side.ikdaman.data.toApiResult
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.BookSearchEntity
import project.side.ikdaman.domain.repository.BookRepository
import javax.inject.Inject

val TAG = "BookRepositoryImpl"

class BookRepositoryImpl @Inject constructor(private val bookService: BookService) : BookRepository {
    override suspend fun searchBookWithTitle(
        title: String,
        startPage: Int
    ): ApiResult<BookSearchEntity> {
        try {
            val response = bookService.searchBookWithTitle(
                query = title,
                startPage = startPage
            )
            return toApiResult(response)
        } catch (e: Exception) {
            Log.e(TAG, "searchBookWithTitle: $e")
            return ApiResult.Error("Unknown error")
        }
    }

    override suspend fun searchBookWithIsbn(isbn: String): ApiResult<BookSearchEntity> {
        try {
            val response = bookService.searchBookWithIsbn(itemId = isbn)
            if (response.isSuccessful && response.body() != null) {
                return ApiResult.Error("Unknown error")
            } else {
                return ApiResult.Error("Unknown error")
            }
        } catch (_: Exception) {
            return ApiResult.Error("Unknown error")
        }
    }
}