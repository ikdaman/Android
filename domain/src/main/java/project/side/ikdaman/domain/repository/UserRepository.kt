package project.side.ikdaman.domain.repository

import kotlinx.coroutines.flow.Flow
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.UserInfo

interface UserRepository {
    suspend fun getNickName(): Flow<String?>

    suspend fun getProvider(): String?

    suspend fun clearToken()

    suspend fun checkNickname(nickname: String): ApiResult<Boolean>

    suspend fun getUserInfo(): ApiResult<UserInfo>

    suspend fun updateUserInfo(userInfo: UserInfo): ApiResult<Unit>

    suspend fun logout(): ApiResult<Unit>

    suspend fun withdraw(): ApiResult<Unit>

    suspend fun autoLogin(): ApiResult<Unit>

    suspend fun reissueToken(): ApiResult<Unit>
}