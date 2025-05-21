package project.side.ikdaman.feature.login

data class SocialLoginResult(
    val isSuccess: Boolean,
    val socialAccessToken: String? = null,
    val provider: String? = null,
    val providerId: String? = null,
    val errorMessage: String? = null
)

sealed class LoginState {
    data object Init : LoginState()
    data object Loading : LoginState()
    data object Success : LoginState()
    data class Error(val message: String) : LoginState()
}