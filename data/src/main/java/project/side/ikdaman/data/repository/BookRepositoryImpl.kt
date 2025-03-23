package project.side.ikdaman.data.repository

import android.util.Log
import project.side.ikdaman.data.data_source.BookItemModelMapper
import project.side.ikdaman.data.service.BookService
import project.side.ikdaman.domain.model.BookSearch
import project.side.ikdaman.domain.repository.BookRepository

import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(private val bookService: BookService): BookRepository {
    override suspend fun searchBookWithTitle(title: String): BookSearch {
        try {
            val response = bookService.searchBookWithTitle(query = title)
            Log.d("hkhk", "searchBookWithTitle: $response")
            return BookItemModelMapper.mapToDomainModel(response)
        } catch (e: Exception) {
            Log.d("hkhk", "searchBookWithTitle 오류: $e")
            return BookSearch()
        }
    }
}