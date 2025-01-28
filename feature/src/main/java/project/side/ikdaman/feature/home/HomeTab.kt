@file:OptIn(ExperimentalMaterial3Api::class)

package project.side.ikdaman.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import project.side.ikdaman.core.navigation.BARCODE_ROUTE
import project.side.ikdaman.core.navigation.BOOK_DETAIL_ROUTE
import project.side.ikdaman.core.navigation.BOOK_EDIT_ROUTE
import project.side.ikdaman.core.navigation.MAIN_ROUTE
import project.side.ikdaman.core.navigation.SEARCH_ROUTE
import project.side.ikdaman.core.ui.AppTheme

@Composable
fun HomeTab(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(
        navController.getBackStackEntry(MAIN_ROUTE)
    )
) {
    val count = viewModel.count.collectAsState()
    HomeTabUI(
        count,
        onNavigateTo = {
            navController.navigate(it)
        }
    )
}

@Composable
fun HomeTabUI(
    count: State<Int> = remember { mutableIntStateOf(0) },
    onNavigateTo: (String) -> Unit = {}
) {
    val addBookDialogState = remember { mutableStateOf(false) }

    Box(
        Modifier.fillMaxSize()
    ) {
        Column(
            Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("책 개수: ${count.value}")
            Button(
                onClick = {
                    addBookDialogState.value = true
                }
            ) {
                Text("새 책 추가하기")
            }

            Button(
                onClick = {
                    onNavigateTo(BOOK_DETAIL_ROUTE)
                }
            ) {
                Text("책 상세화면 이동")
            }
        }

        if (addBookDialogState.value) {
            ModalBottomSheet(
                onDismissRequest = {
                    addBookDialogState.value = false
                }
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("책 등록하기")
                    Button(
                        onClick = {
                            onNavigateTo(SEARCH_ROUTE)
                        }
                    ) {
                        Text("검색해서 등록")
                    }
                    Button(
                        onClick = {
                            onNavigateTo(BARCODE_ROUTE)
                        }
                    ) {
                        Text("바코드로 등록")
                    }
                    Button(
                        onClick = {
                            onNavigateTo(BOOK_EDIT_ROUTE)
                        }
                    ) {
                        Text("직접 입력")
                    }
                }
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