package project.side.ikdaman.domain.repository

import kotlinx.coroutines.flow.Flow

interface PinningBookRepository {
    fun getPinningBook(): Flow<Set<String>>
    fun setPinningBook(bookId: String): Flow<Boolean>
    fun removePinningBook(bookId: String): Flow<Boolean>
}