package project.side.ikdaman.domain.usecase

import project.side.ikdaman.domain.repository.BookApiRepository
import javax.inject.Inject

class GetReadingBooksUseCase @Inject constructor(
    private val bookApiRepository: BookApiRepository
) {
    operator fun invoke() = bookApiRepository.getBooks()
}