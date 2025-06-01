package project.side.ikdaman.data.service

import project.side.ikdaman.app.data.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("ttb/api/ItemSearch.aspx")
    suspend fun searchBookWithTitle(
        @Query("ttbkey") ttbkey: String = BuildConfig.TTB_KEY,
        @Query("Query") query: String,
        @Query("QueryType") queryType: String = "Title",
        @Query("output") output: String = "js",
        @Query("Version") version: String = "20131101"
    ): BookSearchWithTitleResponse

    @GET("ttb/api/ItemLookUp.aspx")
    suspend fun searchBookWithIsbn(
        @Query("ttbkey") ttbkey: String = BuildConfig.TTB_KEY,
        @Query("ItemId") itemId: String,
        @Query("itemIdType") itemIdType: String = "ISBN13",
        @Query("output") output: String = "js",
        @Query("Version") version: String = "20131101"
    ): BookSearchWithIsbnResponse
}

data class BookSearchWithTitleResponse(
    val totalResults: Int,
    val item: List<BookSearchWithTitleItemResponse>
)

data class BookSearchWithTitleItemResponse(
    val title: String,
    val author: String,
    val cover: String,
    val publisher: String,
    val isbn: String?,
    val isbn13: String?,
)

data class BookSearchWithIsbnResponse(
    val totalResults: Int,
    val item: List<BookSearchWithIsbnItemResponse>
)

data class BookSearchWithIsbnItemResponse(
    val title: String,
    val author: String,
    val cover: String,
    val publisher: String,
    val isbn: String?,
    val isbn13: String?,
    val subInfo: BookSubInfoResponse
)

data class BookSubInfoResponse(
    val itemPage: String
)