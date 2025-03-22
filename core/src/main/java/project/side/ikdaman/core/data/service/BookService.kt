package project.side.ikdaman.core.data.service

import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("ttb/api/ItemSearch.aspx")
    suspend fun searchBookWithTitle(
        @Query("ttbkey") ttbkey: String = "ttbgju060611831001",
        @Query("Query") query: String,
        @Query("QueryType") queryType: String = "Title",
        @Query("output") output: String = "JS"
    ): BookSearchResponse
}

data class BookSearchResponse(
    val totalResults:Int,
    val item: List<BookItem>
)

data class BookItem(
    val title: String,
    val author: String,
    val cover: String
)