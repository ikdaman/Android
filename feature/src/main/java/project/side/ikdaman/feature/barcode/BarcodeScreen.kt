package project.side.ikdaman.feature.barcode

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import project.side.ikdaman.core.navigation.BOOK_EDIT_ROUTE
import project.side.ikdaman.core.ui.AppTheme

@Composable
fun BarcodeScreen(navController: NavController) {
    BarcodeScreenUI(
        onBack = {
            navController.popBackStack()
        },
        onNavigateToEditScreen = {
            navController.navigate(BOOK_EDIT_ROUTE)
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BarcodeScreenUI(
    onBack: () -> Unit = {},
    onNavigateToEditScreen: () -> Unit = {}
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
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Barcode Screen")
            Button(
                onClick = onNavigateToEditScreen
            ) {
                Text("선택되면 자동 이동")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun BarcodeScreenUIPreview() {
    AppTheme { BarcodeScreenUI() }
}