package project.side.ikdaman.domain.usecase

import project.side.ikdaman.domain.repository.MyBooksApiRepository
import javax.inject.Inject

class GetBookDetailLogUseCase @Inject constructor(
    private val bookApiRepository: MyBooksApiRepository
) {
    operator fun invoke(bookId: String, page: Int = 1, limit: Int = 20) = bookApiRepository.getBookLog(bookId, page, limit)
}