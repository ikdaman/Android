package project.side.ikdaman.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import project.side.ikdaman.data.model.book.BookCompleted
import project.side.ikdaman.data.model.book.BookThink
import project.side.ikdaman.data.model.book.HomeBook
import project.side.ikdaman.data.service.MyBookApi
import project.side.ikdaman.data.utils.TimeUtils
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.BookDetail
import project.side.ikdaman.domain.model.BookLog
import project.side.ikdaman.domain.model.HomeBookItem
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

    override fun getBookInfo(bookId: String): Flow<ApiResult<BookDetail>> {
        TODO("Not yet implemented")
    }

    override fun deleteBook(bookId: String): Flow<ApiResult<Unit>> {
        TODO("Not yet implemented")
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
            emit(ApiResult.Error("" ?: ""))
        }
    }.catch {
        Log.e("BookApiRepository", "Error fetching books: ${it.message}", it)
        emit(ApiResult.Error("Network error: ${it.message}"))
    }

    override fun addThink(bookId: String, content: String, page: Int): Flow<ApiResult<Unit>> = flow {
        emit(ApiResult.Loading)
        val response = api.addThink(bookId, BookThink(content, page, TimeUtils.nowUTC()))
        if (response.isSuccess()) {
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
        val response = api.addCompleted(bookId, BookCompleted(content, TimeUtils.nowUTC()))
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