package project.side.ikdaman.data.repository

import kotlinx.coroutines.flow.Flow
import project.side.ikdaman.data.service.PinningBookService
import project.side.ikdaman.domain.repository.PinningBookRepository

class PinningBookRepositoryImpl (
    private val pinningBookService: PinningBookService
) : PinningBookRepository {

    override fun getPinningBook(): Flow<Set<String>> {
        return pinningBookService.pinnedItems
    }

    override fun setPinningBook(bookId: String): Flow<Boolean> {
        return pinningBookService.pinItem(bookId)
    }

    override fun removePinningBook(bookId: String): Flow<Boolean> {
        return pinningBookService.unpinItem(bookId)
    }
}