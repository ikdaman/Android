package project.side.ikdaman.data.data_source.remote

import project.side.ikdaman.app.data.BuildConfig
import project.side.ikdaman.data.model.BookSearchDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("ttb/api/ItemSearch.aspx")
    suspend fun searchBookWithTitle(
        @Query("ttbkey") ttbkey: String = BuildConfig.TTB_KEY,
        @Query("query") query: String,
        @Query("queryType") queryType: String = "Title",
        @Query("cover") cover: String = "Big",
        @Query("output") output: String = "js",
        @Query("version") version: String = "20131101",
        @Query("maxResults") maxResults: Int = 50,
        @Query("start") startPage: Int = 1
    ): Response<BookSearchDto>

    @GET("ttb/api/ItemLookUp.aspx")
    suspend fun searchBookWithIsbn(
        @Query("ttbkey") ttbkey: String = BuildConfig.TTB_KEY,
        @Query("ItemId") itemId: String,
        @Query("itemIdType") itemIdType: String = "ISBN13",
        @Query("cover") cover: String = "Big",
        @Query("output") output: String = "js",
        @Query("Version") version: String = "20131101"
    ): Response<BookSearchDto>
}

