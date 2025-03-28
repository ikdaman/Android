package project.side.ikdaman.feature.mypage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import project.side.ikdaman.core.ui.AppTheme

@Suppress("UNUSED_PARAMETER")
@Composable
fun MyPageTab(navController: NavController) {
    MyPageTabUI()
}

@Composable
fun MyPageTabUI() {
    Box(Modifier.fillMaxSize()) {
        Text(text = "MyPage", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
@Preview(showBackground = true)
fun MyPageTabUIPreview() {
    AppTheme { MyPageTabUI() }
}