package project.side.ikdaman.domain.usecase

import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.repository.AuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun login(token: String, provider: String, providerId: String): ApiResult<Unit> {
        return authRepository.login(token, provider, providerId)
    }
}