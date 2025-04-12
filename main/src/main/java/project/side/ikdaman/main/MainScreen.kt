@file:OptIn(ExperimentalMaterial3Api::class)

package project.side.ikdaman.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import project.side.ikdaman.core.navigation.BARCODE_ROUTE
import project.side.ikdaman.core.navigation.BOOKSHELF_ROUTE
import project.side.ikdaman.core.navigation.HOME_ROUTE
import project.side.ikdaman.core.navigation.MY_PAGE_ROUTE
import project.side.ikdaman.core.navigation.SEARCH_ROUTE
import project.side.ikdaman.feature.bookshelf.BookShelfTab
import project.side.ikdaman.feature.home.HomeTab
import project.side.ikdaman.feature.mypage.MyPageTab

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(appNavController: NavHostController) {
    val mainNavController = rememberNavController()
    val addBookDialogState = remember { mutableStateOf(false) }

    Scaffold(bottomBar = {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    mainNavController.popBackStack()
                    mainNavController.navigate(BOOKSHELF_ROUTE) {
                        restoreState = false
                    }
                }
            ) {
                Text("내 책장")
            }
            Button(
                onClick = {
                    appNavController.navigate(SEARCH_ROUTE)
                }
            ) {
                Text("검색")
            }
            Button(
                onClick = {
                    mainNavController.popBackStack()
                    mainNavController.navigate(HOME_ROUTE) {
                        restoreState = false
                    }
                }
            ) {
                Text("홈")
            }
            Button(
                onClick = {
                    mainNavController.popBackStack()
                    mainNavController.navigate(MY_PAGE_ROUTE) {
                        restoreState = false
                    }
                }
            ) {
                Text("마이페이지")
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
                            appNavController.navigate(SEARCH_ROUTE)
                        }
                    ) {
                        Text("검색해서 등록")
                    }
                    Button(
                        onClick = {
                            appNavController.navigate(BARCODE_ROUTE)
                        }
                    ) {
                        Text("바코드로 등록")
                    }
                }
            }
        }
    }) {
        NavHost(navController = mainNavController, startDestination = HOME_ROUTE) {
            composable(HOME_ROUTE) {
                HomeTab(appNavController)
            }
            composable(BOOKSHELF_ROUTE) {
                BookShelfTab(appNavController)
            }
            composable(MY_PAGE_ROUTE) {
                MyPageTab(appNavController)
            }
        }
    }
}