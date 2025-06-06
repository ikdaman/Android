package project.side.ikdaman.domain.repository

import kotlinx.coroutines.flow.Flow
import project.side.ikdaman.domain.model.ApiResult

interface UserRepository {
    suspend fun getNickName(): Flow<String?>

    suspend fun checkNickname(nickname: String): ApiResult<Boolean>
}