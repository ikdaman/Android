package project.side.ikdaman.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import project.side.ikdaman.data.model.book.PostBookRequestBody
import project.side.ikdaman.data.service.BookApiService
import project.side.ikdaman.domain.model.AddBookItem
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.repository.BookApiRepository
import java.time.Instant
import java.time.format.DateTimeFormatter

class BookApiRepositoryImpl(private val api: BookApiService) : BookApiRepository {
    override fun getBooks() = flow {
        emit(ApiResult.Loading)
        val response = api.getReadingBookList()
        if (response.isSuccess()) {
            val books = response.books?.map { it.transformToDomain() }
            if (books != null) {
                emit(ApiResult.Success(books))
            } else {
                emit(ApiResult.Error("Books data is missing"))
            }
        } else {
            emit(ApiResult.Error(response.message ?: ""))
        }
    }.catch {
        Log.e("BookApiRepository", "Error fetching books: ${it.message}", it)
        emit(ApiResult.Error("Network error: ${it.message}"))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun postBook(addBookItem: AddBookItem) = flow {
        Log.d("hkhk", "postBook 호출")
        emit(ApiResult.Loading)
        val postBookRequestBody = PostBookRequestBody(
            title = addBookItem.bookItem.title,
            writer = addBookItem.bookItem.author,
            publisher = addBookItem.bookItem.publisher,
            isbn = addBookItem.bookItem.isbn,
            page = addBookItem.bookItem.subInfo?.itemPage?.toIntOrNull() ?: 0,
            coverImage = addBookItem.bookItem.cover,
            impression = addBookItem.impression,
            createdAt = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        )
        val response = api.postBook(postBookRequestBody)
        if (response.code() == 201) {
            emit(ApiResult.Success(Unit))
        } else {
            emit(ApiResult.Error(response.message() ?: ""))
        }
    }.catch {
        Log.e("BookApiRepository", "Error posting books: ${it.message}", it)
        emit(ApiResult.Error("Network error: ${it.message}"))
    }
}