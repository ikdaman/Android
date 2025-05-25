package project.side.ikdaman.data.repository

import project.side.ikdaman.data.data_source.AuthDataStore
import project.side.ikdaman.data.service.AuthService
import project.side.ikdaman.data.service.LoginRequest
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val authDataStore: AuthDataStore,
) : AuthRepository {

    override suspend fun login(
        token: String,
        provider: String,
        providerId: String
    ): ApiResult<Unit> {
        return try {
            val response = authService.login(token, LoginRequest(provider, providerId))
            if (response.isSuccessful) {
                val header = response.headers()
                val authorization = header["Authorization"]
                val refreshToken = header["refresh-token"]

                response.body()?.let {
                    if (!authorization.isNullOrBlank() && !refreshToken.isNullOrBlank()) {
                        authDataStore.saveAuthInfo(authorization, refreshToken, it.nickname ?: "")
                        ApiResult.Success(Unit)
                    } else ApiResult.Error("토큰이 비어있습니다.")
                } ?: ApiResult.Error("응답이 비어있습니다.")
            } else {
                ApiResult.Error("서버 오류: ${response.code()}, ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("네트워크 오류: ${e.message}")
        }
    }
}