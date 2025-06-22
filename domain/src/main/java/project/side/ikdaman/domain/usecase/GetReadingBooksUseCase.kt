package project.side.ikdaman.domain.usecase

import project.side.ikdaman.domain.repository.MyBooksApiRepository
import javax.inject.Inject

class GetReadingBooksUseCase @Inject constructor(
    private val bookApiRepository: MyBooksApiRepository
) {
    operator fun invoke() = bookApiRepository.getBooks()
}