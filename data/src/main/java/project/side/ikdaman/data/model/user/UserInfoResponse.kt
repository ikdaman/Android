package project.side.ikdaman.data.model.user

import project.side.ikdaman.domain.model.UserInfo

data class UserInfoResponse(
    val nickname: String = "",
    val birthdate: String? = null,
    val gender: String? = null,
    val code: Int = 0,
    val message: String = ""
)

fun UserInfoResponse.toDomain(): UserInfo {
    return UserInfo(
        nickname = this.nickname,
        birthdate = this.birthdate ?: "",
        gender = this.gender ?: ""
    )
}