package project.side.ikdaman.data.service

import project.side.ikdaman.data.model.ApiResponse
import project.side.ikdaman.data.model.book.HomeBook
import project.side.ikdaman.data.model.book.PostBookRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BookApiService {

    @GET("mybooks/in-progress")
    suspend fun getReadingBookList(): ApiResponse<List<HomeBook>>

    @POST("mybooks")
    suspend fun postBook(@Body postBookRequestBody: PostBookRequestBody): Response<Unit>
}