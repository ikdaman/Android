package project.side.ikdaman.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.Api
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.usecase.AutoLoginUseCase
import project.side.ikdaman.domain.usecase.ReissueTokenUseCase
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val autoLoginUseCase: AutoLoginUseCase,
    private val reissueTokenUseCase: ReissueTokenUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        autoLogin()
    }

    private fun autoLogin() {
        viewModelScope.launch {
            when (autoLoginUseCase()) {
                is ApiResult.Loading -> _uiState.value = SplashUiState.Loading
                is ApiResult.Success -> _uiState.value = SplashUiState.GoToHome
                is ApiResult.Error -> reissueToken()
            }
        }
    }

    private suspend fun reissueToken() {
        when (reissueTokenUseCase()) {
            is ApiResult.Loading -> _uiState.value = SplashUiState.Loading
            is ApiResult.Success -> _uiState.value = SplashUiState.GoToHome
            is ApiResult.Error -> _uiState.value = SplashUiState.GoToLogin
        }
    }
}

sealed class SplashUiState {
    data object Loading : SplashUiState()
    data object GoToLogin : SplashUiState()
    data object GoToHome : SplashUiState()
}