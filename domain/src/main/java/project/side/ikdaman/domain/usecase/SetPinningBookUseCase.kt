package project.side.ikdaman.domain.usecase

import project.side.ikdaman.domain.repository.PinningBookRepository
import javax.inject.Inject

class SetPinningBookUseCase @Inject constructor(
    private val pinningBookRepository: PinningBookRepository
) {
    suspend operator fun invoke(bookId: String): Boolean {
        return pinningBookRepository.setPinningBook(bookId)
    }
}