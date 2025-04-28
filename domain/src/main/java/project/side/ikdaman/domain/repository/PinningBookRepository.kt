package project.side.ikdaman.domain.repository

import kotlinx.coroutines.flow.Flow

interface PinningBookRepository {
    fun getPinningBook(): Flow<Set<String>>
    fun setPinningBook(isbn: String): Flow<Boolean>
    fun removePinningBook(isbn: String): Flow<Boolean>
}