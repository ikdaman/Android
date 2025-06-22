package project.side.ikdaman.domain.usecase

import project.side.ikdaman.domain.repository.MyBooksApiRepository
import javax.inject.Inject

class GetBookDetailUseCase @Inject constructor(
    private val bookApiRepository: MyBooksApiRepository
) {
    operator fun invoke(bookId: String) = bookApiRepository.getBookInfo(bookId)
}