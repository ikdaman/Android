package project.side.ikdaman.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.repository.UserRepository
import project.side.ikdaman.domain.usecase.CheckNicknameUseCase
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val checkNicknameUseCase: CheckNicknameUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserInfoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getNickName().collect {
                _uiState.value = _uiState.value.copy(nickname = it ?: "")
            }
        }
    }

    fun checkNickname(nickname: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            when (val result = checkNicknameUseCase(nickname)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        nickState = if (result.data) NickState.VALID else NickState.INVALID
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
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
    val isLoading: Boolean = false,
    val nickname: String = "",
    val nickState: NickState = NickState.INIT,
    val message: String = ""
)

enum class NickState { INIT, CHECK, VALID, INVALID, ERROR }