package project.side.ikdaman.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getNickName(): Flow<String?>
}