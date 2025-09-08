package project.side.ikdaman.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.UserInfo
import project.side.ikdaman.domain.usecase.CheckNicknameUseCase
import project.side.ikdaman.domain.usecase.GetUserInfoUseCase
import project.side.ikdaman.domain.usecase.UpdateUserInfoUseCase
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val checkNicknameUseCase: CheckNicknameUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserInfoUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

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
                        isLoading = false,
                        userInfo = result.data
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = result.message
                    )
                    _uiEvent.emit("정보를 불러오는 데 실패했습니다.")
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
                        nicknameIsUnique = result.data,
                        nicknameIsChecked = true,
                        message = if (!result.data) "사용할 수 없는 닉네임입니다." else ""
                    )
                    if (result.data) _uiEvent.emit("사용 가능한 닉네임입니다.")
                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _uiEvent.emit("오류가 발생했습니다. 잠시 후 다시 시도해 주세요.")
                }

                is ApiResult.Loading -> Unit
            }
        }
    }

    fun updateUserInfo(nickname: String, birthdate: String, gender: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            when (updateUserInfoUseCase(nickname, birthdate, gender)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, isUpdated = true)
                    _uiEvent.emit("변경되었습니다.")
                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, isUpdated = false)
                    _uiEvent.emit("오류가 발생했습니다. 잠시 후 다시 시도해 주세요.")
                }

                is ApiResult.Loading -> Unit
            }
        }
    }

    fun updateNicknameIsValid(nickname: String) {
        val regex = Regex("^[가-힣a-zA-Z0-9]{1,15}\$")
        val isValid = nickname.isNotBlank() && nickname.matches(regex)
        val isPrev = nickname == uiState.value.userInfo.nickname
        _uiState.value = _uiState.value.copy(
            nicknameIsValid = isValid,
            nicknameIsChecked = isPrev,
            nicknameIsUnique = isPrev,
            nicknameIsPrev = isPrev,
            message = if (!isValid) "닉네임은 한글/영어/숫자 조합으로 15자 이내로 작성해주세요." else ""
        )
    }

    fun updateBirthdateIsValid(birthdate: String) {
        _uiState.value = _uiState.value.copy(
            birthdateIsValid = birthdate.isEmpty() || isValidDate(birthdate)
        )
    }
}

data class UserInfoUiState(
    val isLoading: Boolean = true,
    val isUpdated: Boolean = false,
    val userInfo: UserInfo = UserInfo(),
    val nicknameIsValid: Boolean = true,        // 닉네임 유효성 검사
    val nicknameIsUnique: Boolean = true,       // 중복확인 결과
    val nicknameIsChecked: Boolean = true,      // 중복확인 여부
    val nicknameIsPrev: Boolean = true,         // 현재 닉네임인지 확인
    val birthdateIsValid: Boolean = true,
    val message: String = ""
)

enum class Gender {
    MALE, FEMALE, NONE;

    fun genderToString(): String {
        return when (this) {
            MALE -> "MALE"
            FEMALE -> "FEMALE"
            NONE -> ""
        }
    }

    companion object {
        fun toGender(str: String): Gender {
            return when (str) {
                "male", "MALE" -> MALE
                "female", "FEMALE" -> FEMALE
                else -> NONE
            }
        }
    }
}