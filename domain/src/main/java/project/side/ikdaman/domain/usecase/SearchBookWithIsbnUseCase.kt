package project.side.ikdaman.domain.usecase

import project.side.ikdaman.domain.repository.BookRepository
import javax.inject.Inject

class SearchBookWithIsbnUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(isbn: String) = bookRepository.searchBookWithIsbn(isbn)
}