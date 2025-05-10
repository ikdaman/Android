package project.side.ikdaman.data.service

//import project.side.ikdaman.data.Secret
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    // todo ttbKey 되돌리기

    @GET("ttb/api/ItemSearch.aspx")
    suspend fun searchBookWithTitle(
        @Query("ttbkey") ttbkey: String = "ttbgju060611831002",
        @Query("Query") query: String,
        @Query("QueryType") queryType: String = "Title",
        @Query("output") output: String = "js",
        @Query("Version") version: String = "20131101"
    ): BookSearchResponse

    @GET("ttb/api/ItemLookUp.aspx")
    suspend fun searchBookWithIsbn(
        @Query("ttbkey") ttbkey: String = "ttbgju060611831002",
        @Query("ItemId") itemId: String,
        @Query("itemIdType") itemIdType: String = "ISBN13",
        @Query("output") output: String = "js",
        @Query("Version") version: String = "20131101"
    ): BookSearchResponse
}

data class BookSearchResponse(
    val totalResults:Int,
    val item: List<BookItemResponse>
)

data class BookItemResponse(
    val title: String,
    val author: String,
    val cover: String,
    val isbn: String?,
    val isbn13: String?
)