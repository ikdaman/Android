package project.side.ikdaman.feature.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.navigation.LOGIN_ROUTE
import project.side.ikdaman.core.navigation.MAIN_ROUTE

@Composable
fun SplashScreen(navController: NavController, viewModel: SplashViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(uiState) {
        if (uiState == SplashUiState.Loading) return@LaunchedEffect

        delay(1000L) // 스플래시 최소 유지 시간
        when (uiState) {
            is SplashUiState.GoToLogin -> navigateToLoginScreen(navController)
            is SplashUiState.GoToHome -> navigateToHomeScreen(navController)
            else -> Unit
        }
    }

    SplashScreenUI()
}

@Composable
fun SplashScreenUI(visible: MutableState<Boolean> = remember { mutableStateOf(false) }) {
    LaunchedEffect(Unit) {
        visible.value = true
    }

    AnimatedVisibility(visible.value, enter = fadeIn()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.app_icon),
                contentDescription = "Splash Screen Icon",
                modifier = Modifier.size(300.dp)
            )
        }
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
    SplashScreenUI(remember { mutableStateOf(true) })
}