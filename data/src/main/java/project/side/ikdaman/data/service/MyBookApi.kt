package project.side.ikdaman.data.service

import project.side.ikdaman.data.model.responses.EmptyResponse
import project.side.ikdaman.data.model.responses.BooksResponse
import project.side.ikdaman.data.model.FirstImpression
import project.side.ikdaman.data.model.book.BookCompleted
import project.side.ikdaman.data.model.responses.BookLogResponse
import project.side.ikdaman.data.model.book.BookThink
import project.side.ikdaman.data.model.book.HomeBook
import project.side.ikdaman.data.model.book.UpdateBookThink
import project.side.ikdaman.data.model.responses.BookDetailResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface MyBookApi {

    // 나의 책 기록 조회
    @GET("mybooks/{id}/booklog")
    suspend fun getBookLog(
        id: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<BookLogResponse>

    // 나의 책 정보 조회
    @GET("mybooks/{id}")
    suspend fun getBookInfo(id: String): BookDetailResponse

    // 나의 책 삭제
    @DELETE("mybooks/{id}")
    suspend fun deleteBook(id: String): EmptyResponse

    // 첫 인상 추가
    @POST("mybooks/{id}/impression")
    suspend fun postImpression(id: String, body: FirstImpression): EmptyResponse

    // 독서중인 책 목록 조회
    @GET("mybooks/in-progress")
    suspend fun getReadingBookList(): Response<BooksResponse>

    // 생각 추가
    @POST("mybooks/{id}/booklog")
    suspend fun addThink(id: String, body: BookThink): EmptyResponse

    // 생각 삭제
    @DELETE("mybooks/{id}/booklog{logId}")
    suspend fun deleteThink(id: String, logId: Int): EmptyResponse

    // 생각 수정
    @PUT("mybooks/{id}/booklog{logId}")
    suspend fun updateThink(id: String, logId: Int, body: UpdateBookThink): EmptyResponse

    // 완독 추가
    @POST("mybooks/{id}/completed")
    suspend fun addCompleted(id: String, body: BookCompleted): EmptyResponse

    // 완독 삭제
    @DELETE("mybooks/{id}/booklog/{logId}/completed")
    suspend fun deleteCompleted(id: String, logId: Int): EmptyResponse

    // 완독 수정
    @PUT("mybooks{id}/booklog/{logId}/completed")
    suspend fun updateCompleted(id: String, logId: Int, body: UpdateBookThink): EmptyResponse
}
