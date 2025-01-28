package project.side.ikdaman.feature.bookshelf

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import project.side.ikdaman.core.navigation.BOOK_DETAIL_ROUTE
import project.side.ikdaman.core.ui.AppTheme

@Composable
fun BookShelfTab(navController: NavController) {
    BookShelfTabUI(
        onNavigateTo = {
            navController.navigate(it)
        }
    )
}

@Composable
fun BookShelfTabUI(
    onNavigateTo: (String) -> Unit = {}
) {
    Box(Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "BookShelf")
            Button(
                onClick = {
                    onNavigateTo(BOOK_DETAIL_ROUTE)
                }
            ) {
                Text("상세화면으로 이동")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun BookShelfTabUIPreview() {
    AppTheme {
        BookShelfTabUI()
    }
}