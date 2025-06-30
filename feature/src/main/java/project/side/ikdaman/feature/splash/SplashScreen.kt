package project.side.ikdaman.feature.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import project.side.ikdaman.core.navigation.LOGIN_ROUTE
import project.side.ikdaman.core.navigation.MAIN_ROUTE

@Composable
fun SplashScreen(navController: NavController, viewModel: SplashViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(uiState) {
        if (uiState == SplashUiState.Loading) return@LaunchedEffect

        delay(700L) // 스플래시 최소 유지 시간
        when (uiState) {
            is SplashUiState.GoToLogin -> navigateToLoginScreen(navController)
            is SplashUiState.GoToHome -> navigateToHomeScreen(navController)
            else -> Unit
        }
    }

    SplashScreenUI()
}

@Composable
fun SplashScreenUI() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("splash")
    }
}

private fun navigateToLoginScreen(navController: NavController) {
    navController.navigate(LOGIN_ROUTE) {
        popUpTo(0) { inclusive = true }
    }
}

private fun navigateToHomeScreen(navController: NavController) {
    navController.navigate(MAIN_ROUTE) {
        popUpTo(0) { inclusive = true }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreenUI()
}