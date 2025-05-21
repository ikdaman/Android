package project.side.ikdaman.data.service

import project.side.ikdaman.data.model.ApiResponse
import project.side.ikdaman.data.model.book.HomeBook
import retrofit2.http.GET

interface BookApiService {

    @GET("mybooks/in-progress")
    suspend fun getReadingBookList(): ApiResponse<List<HomeBook>>
}
