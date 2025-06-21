package project.side.ikdaman.domain.usecase

import project.side.ikdaman.domain.repository.AuthRepository
import project.side.ikdaman.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(token: String, provider: String, providerId: String) =
        authRepository.login(token, provider, providerId)
}

class LogoutUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.logout()
}

class WithdrawUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.withdraw()
}

class GetProviderUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.getProvider()
}

class ClearTokenUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.clearToken()
}
