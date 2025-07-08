package project.side.ikdaman.feature.bookedit

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun BookEditScreen(
    navController: NavController,
) {
    BookEditScreenUI(
        onBack = {
            navController.popBackStack()
        },
        onComplete = {
            navController.popBackStack(MAIN_ROUTE, inclusive = false, saveState = false)
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BookEditScreenUI(
    onBack: () -> Unit = {},
    onComplete: () -> Unit = {}
) {
    Scaffold(
        topBar = {
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
            Modifier.fillMaxSize().padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "책 상세 작성 Screen")
            Button(
                onClick = {
                    onComplete()
                }
            ) {
                Text("완료")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookEditScreenUIPreview() {
    AppTheme { BookEditScreenUI() }
}