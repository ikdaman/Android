package project.side.ikdaman.feature.signup

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import project.side.ikdaman.core.navigation.LOGIN_ROUTE
import project.side.ikdaman.core.navigation.MAIN_ROUTE
import project.side.ikdaman.core.navigation.SIGNUP_ROUTE
import project.side.ikdaman.core.ui.AppTheme

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    SignUpScreenUI(
        onBack = {
            navController.popBackStack()
        },
        onSignUp = { nickname, birthDate, gender ->
            viewModel.signUp(nickname, birthDate, gender) {
                // TODO 가입 완료 화면 띄우기?
                navController.navigate(MAIN_ROUTE) {
                    popUpTo(SIGNUP_ROUTE) {
                        inclusive = true
                    }
                    popUpTo(LOGIN_ROUTE) {
                        inclusive = true
                    }
                }
            }
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignUpScreenUI(
    onBack: () -> Unit = {},
    onSignUp: (String, String, String) -> Unit = { _, _, _ -> }
) {
    val nickNameState = remember { mutableStateOf("") }
    val birthDayState = remember { mutableStateOf("") }
    val genderState = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            // 뒤로가기 버튼
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = "",
                onValueChange = {
                    nickNameState.value = it
                },
                label = { Text("Nick Name") }
            )

            TextField(
                value = "",
                onValueChange = {
                    birthDayState.value = it
                },
                label = { Text("Birth Day") }
            )

            TextField(
                value = "",
                onValueChange = {
                    genderState.value = it
                },
                label = { Text("성별") }
            )

            Button(onClick = {
                onSignUp(nickNameState.value, birthDayState.value, genderState.value)
            }) {
                Text("Sign Up")
            }

        }
    }

}

@Composable
@Preview(showBackground = true)
fun SignUpScreenPreview() {
    AppTheme {
        SignUpScreenUI()
    }
}
