package project.side.ikdaman.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import project.side.ikdaman.core.utils.TimeUTC
import project.side.ikdaman.data.model.book.BookCompleted
import project.side.ikdaman.data.model.book.BookThink
import project.side.ikdaman.data.service.MyBookApi
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.BookLog
import project.side.ikdaman.domain.repository.MyBooksApiRepository

class MyBooksApiRepositoryImpl(private val api: MyBookApi) : MyBooksApiRepository {
    override fun getBookLog(bookId: String, page: Int, limit: Int): Flow<ApiResult<BookLog>> = flow {
        emit(ApiResult.Loading)
        val response = api.getBookLog(bookId, page, limit)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null && body.isSuccess()) {
                emit(ApiResult.Success(body.bookLog!!))
            } else {
                emit(ApiResult.Error("책 로그 데이터가 없습니다."))
            }
        } else {
            emit(ApiResult.Error("오류 발생"))
        }
    }.catch {
        Log.e("BookApiRepository", "Error fetching book log: ${it.message}", it)
        emit(ApiResult.Error("Network error: ${it.message}"))
    }

    override fun getBookInfo(bookId: String) = flow {
        emit(ApiResult.Loading)
        val response = api.getBookInfo(bookId)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null && body.isSuccess()) {
                emit(ApiResult.Success(body))
            } else {
                emit(ApiResult.Error("책 정보가 없습니다."))
            }
        } else {
            emit(ApiResult.Error("오류 발생"))
        }
    }.catch {
        Log.e("BookApiRepository", "Error fetching book info: ${it.message}", it)
        emit(ApiResult.Error("Network error: ${it.message}"))
    }

    override fun deleteBook(bookId: String) = flow {
        val response = api.deleteBook(bookId)
        if (response.isSuccessful) {
            emit(ApiResult.Success(Unit))
        } else {
            emit(ApiResult.Error("오류 발생"))
        }
    }.catch {
        Log.e("BookApiRepository", "Error deleting book: ${it.message}", it)
        emit(ApiResult.Error("Network error: ${it.message}"))
    }

    override fun postImpression(bookId: String, impression: String): Flow<ApiResult<Unit>> {
        TODO("Not yet implemented")
    }


    override fun getBooks() = flow {
        emit(ApiResult.Loading)
        val response = api.getReadingBookList()
        if (response.isSuccessful) {
            val body = response.body()
            val books = body?.books?.map { it.transformToDomain() }
            if (books != null && body.isSuccess()) {
                emit(ApiResult.Success(books))
            } else {
                emit(ApiResult.Error("Books data is missing"))
            }
        } else {
            emit(ApiResult.Error(""))
        }
    }.catch {
        Log.e("BookApiRepository", "Error fetching books: ${it.message}", it)
        emit(ApiResult.Error("Network error: ${it.message}"))
    }

    override fun addThink(bookId: String, content: String, page: Int): Flow<ApiResult<Unit>> = flow {
        emit(ApiResult.Loading)
        val response = api.addThink(bookId, BookThink(content, page, TimeUTC.now()))
        if (response.isSuccessful) {
            emit(ApiResult.Success(Unit))
        } else {
            emit(ApiResult.Error("오류 발생"))
        }
    }.catch {
        Log.e("BookApiRepository", "Error adding think: ${it.message}", it)
        emit(ApiResult.Error("Network error: ${it.message}"))
    }

    override fun deleteThink(bookId: String, logId: Int): Flow<ApiResult<Unit>> {
        TODO("Not yet implemented")
    }

    override fun updateThink(bookId: String, logId: Int, content: String): Flow<ApiResult<Unit>> {
        TODO("Not yet implemented")
    }

    override fun addCompleted(bookId: String, content: String): Flow<ApiResult<Unit>> = flow {
        emit(ApiResult.Loading)
        val response = api.addCompleted(bookId, BookCompleted(content, TimeUTC.now()))
        if (response.isSuccess()) {
            emit(ApiResult.Success(Unit))
        } else {
            emit(ApiResult.Error("오류 발생"))
        }
    }.catch {
        Log.e("BookApiRepository", "Error adding completed: ${it.message}", it)
        emit(ApiResult.Error("Network error: ${it.message}"))
    }

    override fun deleteCompleted(bookId: String): Flow<ApiResult<Unit>> {
        TODO("Not yet implemented")
    }

    override fun updateCompleted(
        bookId: String,
        logId: Int,
        content: String
    ): Flow<ApiResult<Unit>> {
        TODO("Not yet implemented")
    }
}