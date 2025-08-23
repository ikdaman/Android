package project.side.ikdaman.data.data_source.remote

import project.side.ikdaman.data.model.FirstImpression
import project.side.ikdaman.data.model.book.BookCompleted
import project.side.ikdaman.data.model.book.BookThink
import project.side.ikdaman.data.model.book.PostBookRequestBody
import project.side.ikdaman.data.model.book.UpdateBookCompleted
import project.side.ikdaman.data.model.book.UpdateBookThink
import project.side.ikdaman.data.model.responses.BookShelfResponse
import project.side.ikdaman.data.model.responses.BooksResponse
import project.side.ikdaman.domain.model.BookDetail
import project.side.ikdaman.domain.model.BookLog
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MyBookService {

    // 나의 책 기록 조회
    @GET("mybooks/{id}/booklog")
    suspend fun getBookLog(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<BookLog>

    // 나의 책 정보 조회
    @GET("mybooks/{id}")
    suspend fun getBookInfo(@Path("id") id: String): Response<BookDetail>

    // 나의 책 삭제
    @DELETE("mybooks/{id}")
    suspend fun deleteBook(@Path("id") id: String): Response<Unit>

    // 첫 인상 추가
    @POST("mybooks/{id}/impression")
    suspend fun postImpression(
        @Path("id") id: String,
        @Body body: FirstImpression
    ): Response<Unit>

    // 독서중인 책 목록 조회
    @GET("mybooks/in-progress")
    suspend fun getReadingBookList(): Response<BooksResponse>

    // 생각 추가
    @POST("mybooks/{id}/booklog")
    suspend fun addThink(
        @Path("id") id: String,
        @Body body: BookThink
    ): Response<Unit>

    // 생각 삭제
    @DELETE("mybooks/{id}/booklog/{logId}")
    suspend fun deleteThink(
        @Path("id") id: String,
        @Path("logId") logId: Int
    ): Response<Unit>

    // 생각 수정
    @PUT("mybooks/{id}/booklog/{logId}")
    suspend fun updateThink(
        @Path("id") id: String,
        @Path("logId") logId: Int,
        @Body body: UpdateBookThink
    ): Response<Unit>

    // 완독 추가
    @POST("mybooks/{id}/completed")
    suspend fun addCompleted(
        @Path("id") id: String,
        @Body body: BookCompleted
    ): Response<Unit>

    // 완독 삭제
    @DELETE("mybooks/{id}/booklog/{logId}/completed")
    suspend fun deleteCompleted(
        @Path("id") id: String,
        @Path("logId") logId: Int
    ): Response<Unit>

    // 완독 수정
    @PUT("mybooks/{id}/booklog/{logId}/completed")
    suspend fun updateCompleted(
        @Path("id") id: String,
        @Path("logId") logId: Int,
        @Body body: UpdateBookCompleted
    ): Response<Unit>

    @POST("mybooks")
    suspend fun postBook(@Body postBookRequestBody: PostBookRequestBody): Response<Unit>

    // 나의 책 목록 조회
    @GET("/mybooks")
    suspend fun getBookList(
        @Query("status") status: String?,
        @Query("keyword") keyword: String?,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<BookShelfResponse>
}
