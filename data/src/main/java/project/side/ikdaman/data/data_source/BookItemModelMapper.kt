package project.side.ikdaman.data.data_source

import project.side.ikdaman.data.service.BookSearchWithIsbnResponse
import project.side.ikdaman.data.service.BookSearchWithTitleResponse
import project.side.ikdaman.domain.model.BookItem
import project.side.ikdaman.domain.model.BookSearch
import project.side.ikdaman.domain.model.BookSubInfo

object BookItemModelMapper {
    fun mapFromTitleResponse(bookItemModel: BookSearchWithTitleResponse): BookSearch {
        return BookSearch(
            totalBookCount = bookItemModel.totalResults,
            books = bookItemModel.item.map {
                BookItem(
                    title = it.title,
                    author = it.author,
                    cover = it.cover,
                    publisher = it.publisher,
                    isbn = it.isbn13 ?: it.isbn ?: "",
                    itemId = it.itemId
                )
            }
        )
    }

    fun mapFromIsbnResponse(bookItemModel: BookSearchWithIsbnResponse): BookSearch {
        return BookSearch(
            totalBookCount = bookItemModel.totalResults,
            books = bookItemModel.item.map {
                BookItem(
                    title = it.title,
                    author = it.author,
                    cover = it.cover,
                    publisher = it.publisher,
                    isbn = it.isbn13 ?: it.isbn ?: "",
                    itemId = it.itemId,
                    subInfo = BookSubInfo(it.subInfo.itemPage)
                )
            }
        )
    }
}