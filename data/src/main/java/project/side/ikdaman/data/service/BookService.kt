package project.side.ikdaman.data.service

import project.side.ikdaman.app.data.BuildConfig
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
        @Query("version") version: String = "20131101"
    ): BookSearchResponse

    @GET("ttb/api/ItemLookUp.aspx")
    suspend fun searchBookWithIsbn(
        @Query("ttbkey") ttbkey: String = BuildConfig.TTB_KEY,
        @Query("ItemId") itemId: String,
        @Query("itemIdType") itemIdType: String = "ISBN13",
        @Query("cover") cover: String = "Big",
        @Query("output") output: String = "js",
        @Query("Version") version: String = "20131101"
    ): BookSearchResponse
}

data class BookSearchResponse(
    val totalResults: Int,
    val item: List<BookSearchItem>
)

data class BookSearchItem(
    val title: String,
    val link: String,
    val author: String,
    val cover: String,
    val publisher: String,
    val isbn: String?,
    val isbn13: String?,
    val itemId: Long,
    val subInfo: BookSubInfoResponse? = null
)

data class BookSubInfoResponse(
    val itemPage: String? = null
)