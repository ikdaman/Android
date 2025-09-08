package project.side.ikdaman.domain.usecase

import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.UserInfo
import project.side.ikdaman.domain.repository.UserRepository
import javax.inject.Inject

class CheckNicknameUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(nickname: String) = userRepository.checkNickname(nickname)
}

class GetUserInfoUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.getUserInfo()
}

class UpdateUserInfoUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(nickname: String, birthdate: String, gender: String): ApiResult<Unit> {
        val userInfo = UserInfo(
            nickname = nickname,
            birthdate = birthdate,
            gender = gender
        )
        return userRepository.updateUserInfo(userInfo)
    }
}