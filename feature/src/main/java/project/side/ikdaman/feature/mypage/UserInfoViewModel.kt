package project.side.ikdaman.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.UserInfo
import project.side.ikdaman.domain.usecase.CheckNicknameUseCase
import project.side.ikdaman.domain.usecase.GetUserInfoUseCase
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val checkNicknameUseCase: CheckNicknameUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserInfoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getUserInfo()
        }
    }

    private fun getUserInfo() {
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            when (val result = getUserInfoUseCase()) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSuccess = true,
                        isLoading = false,
                        userInfo = result.data
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSuccess = false,
                        isLoading = false,
                        message = result.message
                    )
                }

                is ApiResult.Loading -> Unit
            }
        }
    }

    fun checkNickname(nickname: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            when (val result = checkNicknameUseCase(nickname)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        nickState = if (result.data) NickState.VALID else NickState.INVALID
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        nickState = NickState.ERROR,
                        message = result.message
                    )
                }

                is ApiResult.Loading -> Unit
            }
        }
    }

    fun updateNickState(nickState: NickState) {
        _uiState.value = _uiState.value.copy(
            nickState = nickState
        )
    }
}

data class UserInfoUiState(
    val isSuccess: Boolean = true,
    val isLoading: Boolean = false,
    val userInfo: UserInfo = UserInfo(),
    val nickState: NickState = NickState.INIT,
    val message: String = ""
)

enum class NickState { INIT, CHECK, VALID, INVALID, ERROR }