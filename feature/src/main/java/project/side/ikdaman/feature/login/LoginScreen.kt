package project.side.ikdaman.feature.login

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val context = LocalContext.current
    val loginState = viewModel.loginState.collectAsStateWithLifecycle()

    LaunchedEffect(loginState.value) {
        when (loginState.value) {
            is LoginState.Success -> {
                // TODO 최초 1번만 튜토리얼 화면으로 이동
                navigateToHomeScreen(navController)
            }

            is LoginState.Error -> Toast.makeText(
                context,
                (loginState.value as LoginState.Error).message,
                Toast.LENGTH_SHORT
            ).show()

            else -> {}
        }
    }

    LoginScreenUI(
        isLoading = loginState.value == LoginState.Loading,
        onGoogleLogin = {
            viewModel.googleLogin {
                navigateToTutorialScreen(navController)
            }
        },
        onNaverLogin = {
            viewModel.naverLogin {
                navigateToTutorialScreen(navController)
            }
        },
        onKakaoLogin = {
            viewModel.kakaoLogin(context)
        }
    )
}

private fun navigateToTutorialScreen(navController: NavController) {
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
    isLoading: Boolean = false,
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
            if (isLoading) {
                CircularProgressIndicator()
            }
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