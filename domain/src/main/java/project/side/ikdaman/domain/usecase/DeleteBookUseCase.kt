package project.side.ikdaman.domain.usecase

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.take
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.repository.MyBooksApiRepository
import project.side.ikdaman.domain.repository.PinningBookRepository
import javax.inject.Inject

class DeleteBookUseCase @Inject constructor(
    private val pinningBookRepository: PinningBookRepository,
    private val bookApiRepository: MyBooksApiRepository
) {
    operator fun invoke(bookId: String) = combine(
        pinningBookRepository.removePinningBook(bookId).take(1),
        bookApiRepository.deleteBook(bookId).take(1)
    ) { pinningResult, deleteResult ->
        if (pinningResult && deleteResult is ApiResult.Success) {
            ApiResult.Success(Unit)
        } else {
            ApiResult.Error("Failed to delete book or remove pinning")
        }
    }
}