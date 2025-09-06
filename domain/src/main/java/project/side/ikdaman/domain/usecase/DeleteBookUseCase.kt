package project.side.ikdaman.domain.usecase

import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.repository.MyBooksApiRepository
import project.side.ikdaman.domain.repository.PinningBookRepository
import javax.inject.Inject

class DeleteBookUseCase @Inject constructor(
    private val pinningBookRepository: PinningBookRepository,
    private val bookApiRepository: MyBooksApiRepository
) {
    suspend operator fun invoke(bookId: String): ApiResult<Unit> {
        val pinningResult = pinningBookRepository.removePinningBook(bookId)
        val deleteResult = bookApiRepository.deleteBook(bookId)

        return if (pinningResult && deleteResult is ApiResult.Success) {
            ApiResult.Success(Unit)
        } else {
            ApiResult.Error("Failed to delete book or remove pinning")
        }
    }
}