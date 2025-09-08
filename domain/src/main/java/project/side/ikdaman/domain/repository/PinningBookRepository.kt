package project.side.ikdaman.domain.repository

import kotlinx.coroutines.flow.Flow

interface PinningBookRepository {
    fun getPinningBook(): Flow<Set<String>>
    suspend fun setPinningBook(bookId: String): Boolean
    suspend fun removePinningBook(bookId: String): Boolean
    suspend fun updatePinningBooks(bookIds: Set<String>): Boolean
}