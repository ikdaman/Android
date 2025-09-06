package project.side.ikdaman.domain.usecase

import project.side.ikdaman.domain.repository.PinningBookRepository
import javax.inject.Inject

class RemovePinningBookUseCase @Inject constructor(
    private val pinningBookRepository: PinningBookRepository
) {
    suspend operator fun invoke(bookId: String): Boolean {
        return pinningBookRepository.removePinningBook(bookId)
    }
}