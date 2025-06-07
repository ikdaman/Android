package project.side.ikdaman.data.repository

import kotlinx.coroutines.flow.Flow
import project.side.ikdaman.data.data_source.AuthDataStore
import project.side.ikdaman.data.model.user.toDomain
import project.side.ikdaman.data.service.UserService
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.UserInfo
import project.side.ikdaman.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val userService: UserService
) : UserRepository {
    override suspend fun getNickName(): Flow<String?> = authDataStore.nickname

    override suspend fun checkNickname(nickname: String): ApiResult<Boolean> {
        return try {
            val response = userService.checkNickName(nickname)
            if (response.isSuccessful) {
                response.body()?.let {
                    ApiResult.Success(it.available)
                } ?: ApiResult.Error("응답이 비어있습니다.")
            } else {
                ApiResult.Error("서버 오류: ${response.code()}, ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("네트워크 오류: ${e.message}")
        }
    }

    override suspend fun getUserInfo(): ApiResult<UserInfo> {
        return try {
            val response = userService.getUserInfo()
            if (response.isSuccessful) {
                response.body()?.let {
                    ApiResult.Success(it.toDomain())
                } ?: ApiResult.Error("응답이 비어있습니다.")
            } else {
                ApiResult.Error("서버 오류: ${response.code()}, ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("서버 오류: ${e.message}")
        }
    }

    override suspend fun updateUserInfo(userInfo: UserInfo): ApiResult<Unit> {
        return try {
            val response = userService.updateUserInfo(userInfo)
            if (response.isSuccessful) {
                response.body()?.let {
                    authDataStore.saveNickname(it.nickname)
                    ApiResult.Success(Unit)
                } ?: ApiResult.Error("응답이 비어있습니다.")
            } else ApiResult.Error("서버 오류: ${response.code()}, ${response.message()}")
        } catch (e: Exception) {
            ApiResult.Error("서버 오류: ${e.message}")
        }
    }
}