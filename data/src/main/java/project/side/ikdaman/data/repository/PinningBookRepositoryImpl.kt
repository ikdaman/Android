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

    override suspend fun setPinningBook(bookId: String): Boolean {
        return pinningBookService.pinItem(bookId)
    }

    override suspend fun removePinningBook(bookId: String): Boolean {
        return pinningBookService.unpinItem(bookId)
    }

    override suspend fun updatePinningBooks(bookIds: Set<String>): Boolean {
        return pinningBookService.clearAndAddAll(bookIds)
    }
}