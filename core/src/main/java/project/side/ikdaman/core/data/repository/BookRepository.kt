package project.side.ikdaman.core.data.repository

import android.util.Log
import project.side.ikdaman.core.data.service.BookService
import javax.inject.Inject

class BookRepository @Inject constructor(private val bookService: BookService) {
    suspend fun searchBookWithTitle(title: String) {
        try{
            val response = bookService.searchBookWithTitle(query = title)
            Log.d("hkhk", "searchBookWithTitle: $response")
        }catch (e:Exception){
            Log.d("hkhk", "searchBookWithTitle 오류: $e")
        }
    }
}