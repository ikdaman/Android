package project.side.ikdaman.feature.tutorial

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
import androidx.navigation.NavController
import project.side.ikdaman.core.navigation.MAIN_ROUTE
import project.side.ikdaman.core.ui.AppTheme

@Composable
fun TutorialScreen(
    navController: NavController
) {
    TutorialScreenUI(
        onNavigateToMain = {
            navController.navigate(MAIN_ROUTE)
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TutorialScreenUI(
    onNavigateToMain: () -> Unit = {}
) {
    Scaffold {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("서비스 소개")

            Button(onClick = {
                onNavigateToMain()
            }) {
                Text("홈화면으로 가기")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun TutorialScreenPreview() {
    AppTheme {
        TutorialScreenUI()
    }
}
