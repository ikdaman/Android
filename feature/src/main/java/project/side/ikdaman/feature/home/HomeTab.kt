@file:OptIn(ExperimentalMaterial3Api::class)

package project.side.ikdaman.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import project.side.ikdaman.core.navigation.BOOK_DETAIL_ROUTE
import project.side.ikdaman.core.navigation.MAIN_ROUTE
import project.side.ikdaman.core.ui.AppTheme

@Composable
fun HomeTab(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(
        navController.getBackStackEntry(MAIN_ROUTE)
    )
) {
    @Suppress("UNUSED_VARIABLE")
    val count = viewModel.count.collectAsState()
    HomeTabUI(
        onNavigateTo = {
            navController.navigate(it)
        }
    )
}

@Composable
fun HomeTabUI(
    onNavigateTo: (String) -> Unit = {}
) {
    Box(
        Modifier.fillMaxSize()
    ) {
        Column(
            Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    onNavigateTo(BOOK_DETAIL_ROUTE)
                }
            ) {
                Text("책 상세화면 이동")
            }
            Button(
                onClick = {
                    // TODO 기록 추가 Dialog 표시
                }
            ) {
                Text("기록 추가하기")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeTabPreview() {
    AppTheme {
        HomeTabUI()
    }
}