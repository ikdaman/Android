package project.side.ikdaman.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import project.side.ikdaman.domain.repository.UserRepository
import project.side.ikdaman.domain.usecase.CancelAlarmUseCase
import project.side.ikdaman.domain.usecase.GetSavedAlarmTimeUseCase
import project.side.ikdaman.domain.usecase.ScheduleAlarmUseCase
import javax.inject.Inject

private const val DEFAULT_ALARM_TIME = "21:00"

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val scheduleAlarmUseCase: ScheduleAlarmUseCase,
    private val cancelAlarmUseCase: CancelAlarmUseCase,
    private val getSavedAlarmTimeUseCase: GetSavedAlarmTimeUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getNickName().collect { nickname ->
                _uiState.update { it.copy(nickname = nickname ?: "") }

                loadAlarmState()
            }
        }
    }

    private fun loadAlarmState(callback: () -> Unit = {}) {
        viewModelScope.launch {
            val time = getSavedAlarmTimeUseCase()
            if (time != null) {
                _uiState.update { it.copy(selectedTime = time, isChecked = true) }
            } else {
                _uiState.update { it.copy(selectedTime = DEFAULT_ALARM_TIME) }
            }
            callback()
        }
    }

    fun toggleAlarm() {
        val newState = !_uiState.value.isChecked
        _uiState.update { it.copy(isChecked = newState) }

        if (newState) {
            loadAlarmState {
                scheduleAlarmUseCase(_uiState.value.selectedTime)
            }
        } else {
            cancelAlarmUseCase()
        }
    }

    fun updateSelectedTime(time: String) {
        _uiState.update { it.copy(selectedTime = time) }

        if (_uiState.value.isChecked) {
            cancelAlarmUseCase()
            scheduleAlarmUseCase(time)
        }
    }
}

data class MyPageUiState(
    val nickname: String = "",
    val isChecked: Boolean = false,
    val selectedTime: String = DEFAULT_ALARM_TIME
)