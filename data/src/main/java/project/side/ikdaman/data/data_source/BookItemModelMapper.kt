package project.side.ikdaman.data.data_source

import project.side.ikdaman.data.service.BookSearchResponse
import project.side.ikdaman.domain.model.BookItem
import project.side.ikdaman.domain.model.BookSearch

object BookItemModelMapper {
    fun mapToDomainModel(bookItemModel: BookSearchResponse): BookSearch {
        return BookSearch(
            totalBookCount = bookItemModel.totalResults,
            books = bookItemModel.item.map {
                BookItem(
                    title = it.title,
                    author = it.author,
                    cover = it.cover,
                    isbn = it.isbn13 ?: it.isbn ?: ""
                )
            }
        )
    }
}