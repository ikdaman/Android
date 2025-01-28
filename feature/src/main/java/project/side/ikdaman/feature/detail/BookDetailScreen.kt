package project.side.ikdaman.feature.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import project.side.ikdaman.core.ui.AppTheme

@Composable
fun BookDetailScreen(
    navController: NavController
) {
    BookDetailScreenUI(
        onBack = {
            navController.popBackStack()
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BookDetailScreenUI(
    onBack: () -> Unit = {}
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
        Box(Modifier.fillMaxSize()) {
            Text(text = "Book Detail Screen", modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookDetailScreenUIPreview() {
    AppTheme {
        BookDetailScreenUI()
    }
}