package project.side.ikdaman.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import project.side.ikdaman.data.model.FirstImpression
import project.side.ikdaman.data.model.book.BookCompleted
import project.side.ikdaman.data.model.book.BookThink
import project.side.ikdaman.data.model.book.PostBookRequestBody
import project.side.ikdaman.data.model.book.UpdateBookCompleted
import project.side.ikdaman.data.model.book.UpdateBookThink
import project.side.ikdaman.data.service.MyBookApi
import project.side.ikdaman.domain.model.AddBookItem
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.repository.MyBooksApiRepository
import java.time.Instant
import java.time.format.DateTimeFormatter

class MyBooksApiRepositoryImpl(private val api: MyBookApi) : MyBooksApiRepository {
    override fun getBookLog(bookId: String, page: Int, limit: Int) = flow {
        emit(ApiResult.Loading)
        val response = api.getBookLog(bookId, page, limit)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null && body.isSuccess()) {
                emit(ApiResult.Success(body))
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

    override fun postImpression(bookId: String, impression: String) = flow {
        emit(ApiResult.Loading)
        val response = api.postImpression(bookId, FirstImpression(impression))
        if (response.isSuccessful) {
            emit(ApiResult.Success(Unit))
        } else {
            emit(ApiResult.Error("오류 발생"))
        }
    }.catch {
        Log.e("BookApiRepository", "Error posting impression: ${it.message}", it)
        emit(ApiResult.Error("Network error: ${it.message}"))
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
            emit(ApiResult.Error("Failed to fetch books: ${response.message()}"))
        }
    }.catch {
        Log.e("BookApiRepository", "Error fetching books: ${it.message}", it)
        emit(ApiResult.Error("Network error: ${it.message}"))
    }

    override fun addThink(bookId: String, content: String, page: Int): Flow<ApiResult<Unit>> = flow {
        emit(ApiResult.Loading)
        val response = api.addThink(bookId, BookThink(content, page))
        if (response.isSuccessful) {
            emit(ApiResult.Success(Unit))
        } else {
            emit(ApiResult.Error("오류 발생"))
        }
    }.catch {
        Log.e("MyBooksApiRepositoryImpl", "Error adding think: ${it.message}", it)
        emit(ApiResult.Error("Network error: ${it.message}"))
    }

    override fun deleteThink(bookId: String, logId: Int) = flow {
        emit(ApiResult.Loading)
        val response = api.deleteThink(bookId, logId)
        if (response.isSuccessful) {
            emit(ApiResult.Success(Unit))
        } else {
            emit(ApiResult.Error("오류 발생"))
        }
    }.catch {
        Log.e("MyBooksApiRepositoryImpl", "Error deleting think: ${it.message}", it)
        emit(ApiResult.Error("Network error: ${it.message}"))
    }

    override fun updateThink(bookId: String, logId: Int, content: String) = flow {
        emit(ApiResult.Loading)
        val response = api.updateThink(bookId, logId, UpdateBookThink(content))
        if (response.isSuccessful) {
            emit(ApiResult.Success(Unit))
        } else {
            emit(ApiResult.Error("오류 발생"))
        }
    }.catch {
        Log.e("MyBooksApiRepositoryImpl", "Error updating think: ${it.message}", it)
        emit(ApiResult.Error("Network error: ${it.message}"))
    }

    override fun addCompleted(bookId: String, content: String): Flow<ApiResult<Unit>> = flow {
        emit(ApiResult.Loading)
        val response = api.addCompleted(bookId, BookCompleted(content))
        if (response.isSuccessful) {
            emit(ApiResult.Success(Unit))
        } else {
            emit(ApiResult.Error("오류 발생"))
        }
    }.catch {
        Log.e("MyBooksApiRepositoryImpl", "Error adding completed: ${it.message}", it)
        emit(ApiResult.Error("Network error: ${it.message}"))
    }

    override fun deleteCompleted(bookId: String, logId: Int) = flow {
        emit(ApiResult.Loading)
        val response = api.deleteCompleted(bookId, logId)
        if (response.isSuccessful) {
            emit(ApiResult.Success(Unit))
        } else {
            emit(ApiResult.Error("오류 발생"))
        }
    }.catch {
        Log.e("MyBooksApiRepositoryImpl", "Error deleting completed: ${it.message}", it)
        emit(ApiResult.Error("Network error: ${it.message}"))
    }

    override fun updateCompleted(
        bookId: String,
        logId: Int,
        content: String
    ) = flow {
        emit(ApiResult.Loading)
        val response = api.updateCompleted(bookId, logId, UpdateBookCompleted(content))
        if (response.isSuccessful) {
            emit(ApiResult.Success(Unit))
        } else {
            emit(ApiResult.Error("오류 발생"))
        }
    }.catch {
        Log.e("BookApiRepository", "Error updating completed: ${it.message}", it)
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
            itemId = addBookItem.bookItem.itemId,
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