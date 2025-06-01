package project.side.ikdaman.data.repository

import kotlinx.coroutines.flow.Flow
import project.side.ikdaman.data.data_source.AuthDataStore
import project.side.ikdaman.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val authDataStore: AuthDataStore
): UserRepository {
    override suspend fun getNickName(): Flow<String?> = authDataStore.nickname
}