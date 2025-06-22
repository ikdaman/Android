package project.side.ikdaman.feature.mypage

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.usecase.ClearTokenUseCase
import project.side.ikdaman.domain.usecase.GetProviderUseCase
import project.side.ikdaman.domain.usecase.LogoutUseCase
import project.side.ikdaman.domain.usecase.WithdrawUseCase
import project.side.ikdaman.feature.login.GoogleAuth
import project.side.ikdaman.feature.login.KakaoAuth
import project.side.ikdaman.feature.login.NaverAuth
import project.side.ikdaman.feature.login.SocialLoginResult
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val withdrawUseCase: WithdrawUseCase,
    private val clearTokenUseCase: ClearTokenUseCase,
    private val getProviderUseCase: GetProviderUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<AccountState>(AccountState.Init)
    val uiState = _uiState.asStateFlow()

    private val _provider = MutableStateFlow("")
    val provider = _provider.asStateFlow()

    init {
        getProvider()
    }

    private fun getProvider() {
        viewModelScope.launch {
            _provider.value = getProviderUseCase() ?: ""
        }
    }

    fun initAccountState() {
        _uiState.value = AccountState.Init
    }

    fun logout(context: Context) {
        when (provider.value) {
            "KAKAO" -> handleLogout { KakaoAuth.logout() }
            "NAVER" -> handleLogout { NaverAuth.logout() }
            "GOOGLE" -> handleLogout { GoogleAuth.logout(context) }
            else -> Unit
        }
    }

    fun withdraw() {
        when (provider.value) {
            "KAKAO" -> handleWithdraw { KakaoAuth.unlink() }
            "NAVER" -> handleWithdraw { NaverAuth.unlink() }
            "GOOGLE" -> handleWithdraw { true }
            else -> Unit
        }
    }

    fun reAuth(context: Context) {
        when (provider.value) {
            "KAKAO" -> reAuthAndUnlink(
                loginAction = { KakaoAuth.login(context) },
                unlinkAction = { KakaoAuth.unlink() }
            )

            "NAVER" -> reAuthAndUnlink(
                loginAction = { NaverAuth.login(context) },
                unlinkAction = { NaverAuth.unlink() }
            )

            else -> Unit
        }
    }

    private fun reAuthAndUnlink(
        loginAction: suspend () -> SocialLoginResult,
        unlinkAction: suspend () -> Boolean
    ) {
        _uiState.value = AccountState.Loading

        viewModelScope.launch {
            _uiState.value = try {
                if (loginAction().isSuccess) {    // 소셜로그인만 다시 진행(소셜 토큰 만료 시)
                    if (unlinkAction()) {
                        AccountState.Success(SuccessType.WITHDRAW)
                    } else {    // 서비스 회원 탈퇴 성공, 소셜로그인 연결 해제 실패
                        AccountState.Error(ErrorType.UNLINK_FAILED, navigateToLogin = true)
                    }
                } else {
                    AccountState.Error(ErrorType.RE_AUTH_FAILED)
                }
            } catch (e: Exception) {
                AccountState.Error(ErrorType.UNKNOWN)
            }
        }
    }

    private fun handleLogout(logoutAction: suspend () -> Unit) {
        _uiState.value = AccountState.Loading

        viewModelScope.launch {
            _uiState.value = try {
                when (val result = logoutUseCase()) {
                    is ApiResult.Success -> {
                        logoutAction()  // 소셜 로그아웃
                        clearTokenUseCase()
                        AccountState.Success(SuccessType.LOGOUT)
                    }

                    is ApiResult.Error -> {
                        Log.e("LOGOUT", "SERVER LOGOUT ERROR: ${result.message}")
                        AccountState.Error(ErrorType.LOGOUT_FAILED)
                    }

                    is ApiResult.Loading -> AccountState.Loading
                }
            } catch (e: Exception) {
                AccountState.Error(ErrorType.UNKNOWN)
            }
        }
    }

    private fun handleWithdraw(unlinkAction: suspend () -> Boolean) {
        _uiState.value = AccountState.Loading

        viewModelScope.launch {
            _uiState.value = try {
                when (val result = withdrawUseCase()) {
                    is ApiResult.Success -> {
                        if (unlinkAction()) {           //소셜 연결 해제 성공
                            clearTokenUseCase()
                            AccountState.Success(SuccessType.WITHDRAW)
                        } else {
                            AccountState.NeedToLogin(WarningType.NEED_RE_AUTH)    // 소셜 연결 해제 실패(소셜 토큰 만료 포함) -> 소셜만 재로그인
                        }
                    }

                    is ApiResult.Error -> {
                        Log.e("WITHDRAW", "SERVER WITHDRAW ERROR: ${result.message}")
                        AccountState.Error(ErrorType.WITHDRAW_FAILED)
                    }

                    else -> AccountState.Loading
                }
            } catch (e: Exception) {
                AccountState.Error(ErrorType.UNKNOWN)
            }
        }
    }
}

enum class SuccessType { LOGOUT, WITHDRAW }
enum class WarningType { NEED_RE_AUTH }
enum class ErrorType { LOGOUT_FAILED, RE_AUTH_FAILED, UNLINK_FAILED, WITHDRAW_FAILED, UNKNOWN }

sealed class AccountState {
    data object Init : AccountState()
    data object Loading : AccountState()
    data class Success(val type: SuccessType) : AccountState()
    data class NeedToLogin(val type: WarningType) : AccountState()
    data class Error(val type: ErrorType, val navigateToLogin: Boolean = false) : AccountState()
}