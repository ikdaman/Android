package project.side.ikdaman.domain.repository

import kotlinx.coroutines.flow.Flow
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.BookDetail
import project.side.ikdaman.domain.model.BookLog
import project.side.ikdaman.domain.model.HomeBookItem

interface MyBooksApiRepository {
    fun getBookLog(bookId: String, page: Int, limit: Int): Flow<ApiResult<BookLog>>
    fun getBookInfo(bookId: String): Flow<ApiResult<BookDetail>>
    fun deleteBook(bookId: String): Flow<ApiResult<Unit>>
    fun postImpression(bookId: String, impression: String): Flow<ApiResult<Unit>>
    fun getBooks(): Flow<ApiResult<List<HomeBookItem>>>

    // 생각 추가 삭제 수정
    fun addThink(bookId: String, content: String, page: Int): Flow<ApiResult<Unit>>
    fun deleteThink(bookId: String, logId: Int): Flow<ApiResult<Unit>>
    fun updateThink(bookId: String, logId: Int, content: String): Flow<ApiResult<Unit>>

    // 완독 추가 삭제 수정
    fun addCompleted(bookId: String, content: String): Flow<ApiResult<Unit>>
    fun deleteCompleted(bookId: String, logId: Int): Flow<ApiResult<Unit>>
    fun updateCompleted(bookId: String, logId: Int, content: String): Flow<ApiResult<Unit>>
}