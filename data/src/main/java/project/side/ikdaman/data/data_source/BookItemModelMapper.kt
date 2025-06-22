package project.side.ikdaman.data.data_source

import project.side.ikdaman.data.service.BookSearchResponse
import project.side.ikdaman.data.service.BookSubInfoResponse
import project.side.ikdaman.domain.model.BookItem
import project.side.ikdaman.domain.model.BookSearchResult
import project.side.ikdaman.domain.model.BookSubInfo

object BookItemModelMapper {
    fun BookSearchResponse.toDomain() =
        BookSearchResult(
            totalBookCount = totalResults,
            books = item.map {
                BookItem(
                    title = it.title,
                    author = it.author,
                    cover = it.cover,
                    publisher = it.publisher,
                    isbn = it.isbn13 ?: it.isbn ?: "",
                    itemId = it.itemId,
                    link = it.link,
                    subInfo = it.subInfo.toDomain()
                )
            }
        )

    private fun BookSubInfoResponse?.toDomain() = if (this == null) null else BookSubInfo(itemPage = itemPage)
}