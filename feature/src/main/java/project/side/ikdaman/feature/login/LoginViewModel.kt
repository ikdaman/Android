package project.side.ikdaman.feature.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.usecase.AuthUseCase
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Init)
    val loginState = _loginState.asStateFlow()

    fun googleLogin(callback: () -> Unit) {
        callback()
    }

    fun naverLogin(context: Context) {
        handleLogin {
            NaverAuth.login(context)
        }
    }

    fun kakaoLogin(context: Context) {
        handleLogin {
            KakaoAuth.login(context)
        }
    }

    private fun handleLogin(loginAction: suspend () -> SocialLoginResult) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            try {
                val socialLoginResult = loginAction()
                if (socialLoginResult.isSuccess) {
                    // 서버로 토큰 전송 및 저장
                    val result = authUseCase.login(
                        socialLoginResult.socialAccessToken ?: "",
                        socialLoginResult.provider ?: "",
                        socialLoginResult.providerId ?: ""
                    )

                    // Log -> 개발자 출력용
                    // LoginState.Error -> 사용자 출력용
                    _loginState.value = when(result) {
                        is ApiResult.Success -> LoginState.Success
                        is ApiResult.Error -> {
                            Log.d("AUTH", "SERVER LOGIN ERROR: ${result.message}")
                            LoginState.Error("로그인에 실패했습니다.")
                        }
                        is ApiResult.Loading -> LoginState.Loading
                    }
                } else {
                    if (socialLoginResult.errorMessage.isNullOrBlank()) { // 사용자 취소(뒤로 가기 등)
                        _loginState.value = LoginState.Init
                    } else {
                        Log.d("AUTH", "SOCIAL LOGIN ERROR: ${socialLoginResult.errorMessage}")
                        _loginState.value = LoginState.Error("로그인에 실패했습니다.")
                    }
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("오류가 발생했습니다. 잠시 후 다시 시도해 주세요.")
            }
        }
    }
}