package project.side.ikdaman.feature.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import project.side.ikdaman.core.navigation.BOOK_EDIT_ROUTE
import project.side.ikdaman.core.navigation.MAIN_ROUTE
import project.side.ikdaman.core.ui.AppTheme

@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = hiltViewModel(
        navController.getBackStackEntry(MAIN_ROUTE)
    )
) {
    val totalCountState = searchViewModel.totalCount.collectAsState()

    SearchScreenUI(
        totalCount = totalCountState.value,
        onBack = {
            navController.popBackStack()
        },
        onNavigateToEditScreen = {
            navController.navigate(BOOK_EDIT_ROUTE)
        },
        onSearchKeywordChange = {
            searchViewModel.searchBookWithTitle(it)
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreenUI(
    totalCount: Int = 0,
    onBack: () -> Unit = {},
    onNavigateToEditScreen: () -> Unit = {},
    onSearchKeywordChange: (String) -> Unit = {}
) {
    val searchKeyword = remember { mutableStateOf("") }
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
            Text("총 도서 수: $totalCount")
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextField(
                    value = searchKeyword.value,
                    onValueChange = {
                        searchKeyword.value = it
                    }
                )
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = {
                        onSearchKeywordChange(searchKeyword.value)
                    }
                ) {
                    Text("검색")
                }
            }
            Text(text = "Search Screen")
            Button(
                onClick = onNavigateToEditScreen
            ) {
                Text("책 선택")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SearchScreenUIPreview() {
    AppTheme {
        SearchScreenUI()
    }
}
