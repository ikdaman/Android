package project.side.ikdaman.feature.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    fun googleLogin(callback: () -> Unit) {
        callback()
    }

    fun naverLogin(callback: () -> Unit) {
        callback()
    }

    fun kakaoLogin(callback: () -> Unit) {
        callback()
    }
}