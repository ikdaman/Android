package project.side.ikdaman.feature.signup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    fun signUp(
        nickname: String,
        birthDate: String,
        gender: String,
        callback: () -> Unit
    ) {
        callback()
    }
}