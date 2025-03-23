package project.side.ikdaman.domain.usecase

import project.side.ikdaman.domain.repository.BookRepository
import javax.inject.Inject

class SearchBookWithTitleUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(title: String) = bookRepository.searchBookWithTitle(title)
}