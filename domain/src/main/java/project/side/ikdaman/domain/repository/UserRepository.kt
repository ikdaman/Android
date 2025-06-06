package project.side.ikdaman.domain.repository

import kotlinx.coroutines.flow.Flow
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.UserInfo

interface UserRepository {
    suspend fun getNickName(): Flow<String?>

    suspend fun checkNickname(nickname: String): ApiResult<Boolean>

    suspend fun getUserInfo(): ApiResult<UserInfo>
}