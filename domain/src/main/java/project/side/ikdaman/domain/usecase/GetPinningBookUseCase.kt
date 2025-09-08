package project.side.ikdaman.domain.usecase

import kotlinx.coroutines.flow.flow
import project.side.ikdaman.domain.model.HomeBookItem
import project.side.ikdaman.domain.repository.PinningBookRepository
import javax.inject.Inject

class GetPinningBookUseCase @Inject constructor(
    private val pinningBookRepository: PinningBookRepository
) {
    operator fun invoke(bookList: List<HomeBookItem>) = flow {
        pinningBookRepository.getPinningBook().collect { pinnedBookList ->
            val pinned = bookList.filter { book -> book.id in pinnedBookList }
            val unpinned = bookList.filter { book -> book.id !in pinnedBookList }
            pinningBookRepository.updatePinningBooks(pinned.map { it.id }.toSet())
            emit(Pair(pinned, unpinned))
        }
    }
}