package project.side.ikdaman.feature.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import project.side.ikdaman.core.navigation.LOGIN_ROUTE
import project.side.ikdaman.core.navigation.MAIN_ROUTE
import project.side.ikdaman.core.navigation.TUTORIAL_ROUTE
import project.side.ikdaman.core.ui.AppTheme

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    LoginScreenUI(
        onGoogleLogin = {
            viewModel.googleLogin {
                navigateToSignUpScreen(navController)
            }
        },
        onNaverLogin = {
            viewModel.naverLogin {
                navigateToSignUpScreen(navController)
            }
        },
        onKakaoLogin = {
            viewModel.kakaoLogin {
                // TODO: 로그인 성공 시 홈 화면으로 이동
                navigateToHomeScreen(navController)
            }
        }
    )
}

private fun navigateToSignUpScreen(navController: NavController) {
    navController.navigate(TUTORIAL_ROUTE)
}

private fun navigateToHomeScreen(navController: NavController) {
    navController.navigate(MAIN_ROUTE) {
        popUpTo(LOGIN_ROUTE) {
            inclusive = true
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreenUI(
    onGoogleLogin: () -> Unit = {},
    onNaverLogin: () -> Unit = {},
    onKakaoLogin: () -> Unit = {}
) {
    Scaffold {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = onGoogleLogin) {
                Text("Google Login")
            }
            Button(onClick = onNaverLogin) {
                Text("Naver Login")
            }
            Button(onClick = onKakaoLogin) {
                Text("Kakao Login")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    AppTheme {
        LoginScreenUI()
    }
}