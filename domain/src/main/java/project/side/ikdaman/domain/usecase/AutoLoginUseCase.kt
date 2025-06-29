package project.side.ikdaman.domain.usecase

import project.side.ikdaman.domain.repository.UserRepository
import javax.inject.Inject

class AutoLoginUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.autoLogin()
}

class ReissueTokenUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.reissueToken()
}