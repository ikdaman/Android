package project.side.ikdaman.data.repository

import android.util.Log
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import project.side.ikdaman.data.service.BookApiService
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.repository.BookApiRepository

class BookApiRepositoryImpl(private val api: BookApiService) : BookApiRepository {
    override fun getBooks() = flow {
        emit(ApiResult.Loading)
        val response = api.getReadingBookList()
        if (response.isSuccess()) {
            val books = response.books!!.map { it.transformToDomain() }
            emit(ApiResult.Success(books))
        } else {
            emit(ApiResult.Error(response.message ?: ""))
        }
    }.catch {
        Log.e("BookApiRepository", "Error fetching books: ${it.message}", it)
        emit(ApiResult.Error("Network error: ${it.message}"))
    }
}