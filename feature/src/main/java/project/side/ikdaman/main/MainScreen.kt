@file:OptIn(ExperimentalMaterial3Api::class)

package project.side.ikdaman.main

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.navigation.BARCODE_ROUTE
import project.side.ikdaman.core.navigation.BOOKSHELF_ROUTE
import project.side.ikdaman.core.navigation.HOME_ROUTE
import project.side.ikdaman.core.navigation.MY_PAGE_ROUTE
import project.side.ikdaman.core.navigation.SEARCH_ROUTE
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.feature.bookshelf.BookShelfTab
import project.side.ikdaman.feature.home.HomeTab
import project.side.ikdaman.feature.mypage.MyPageTab

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(appNavController: NavHostController) {
    val mainNavController = rememberNavController()
    val addBookDialogState = remember { mutableStateOf(false) }
    val currentDestination = remember { mutableStateOf(HOME_ROUTE) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { addBookDialogState.value = true },
                containerColor = Color.Transparent,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.floting),
                    contentDescription = "Floating Button",
                    modifier = Modifier.size(45.dp),
                    tint = Color.Unspecified
                )
            }
        },
        bottomBar = {
            BottomTabs(mainNavController, currentDestination) {
                addBookDialogState.value = true
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
                currentDestination.value = HOME_ROUTE
                HomeTab(appNavController)
            }
            composable(BOOKSHELF_ROUTE) {
                currentDestination.value = BOOKSHELF_ROUTE
                BookShelfTab(appNavController)
            }
            composable(MY_PAGE_ROUTE) {
                currentDestination.value = MY_PAGE_ROUTE
                MyPageTab(appNavController)
            }
        }
    }
}

@Composable
private fun BottomTabs(
    mainNavController: NavHostController,
    currentDestination: MutableState<String>,
    onClickAddBook: () -> Unit = {}
) {
    val currentRoute = currentDestination.value
    Column {
        HorizontalDivider(thickness = 1.dp, color = Color(0xFFDDDDDD))
        Row(
            Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                onClick = {
                    mainNavController.popBackStack()
                    mainNavController.navigate(HOME_ROUTE) {
                        restoreState = false
                    }
                }
            ) {
                if (currentRoute == HOME_ROUTE) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.home_enabled),
                        contentDescription = null
                    )
                } else
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.home_disabled),
                        contentDescription = null
                    )
            }

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                elevation = null,
                onClick = {
                    onClickAddBook()
                }
            ) {
                if (currentRoute == SEARCH_ROUTE) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.search_enabled),
                        contentDescription = null
                    )
                } else
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.search_disabled),
                        contentDescription = null
                    )
            }
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                onClick = {
                    mainNavController.popBackStack()
                    mainNavController.navigate(BOOKSHELF_ROUTE) {
                        restoreState = false
                    }
                }
            ) {
                if (currentRoute == BOOKSHELF_ROUTE) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.bookshelf_enabled),
                        contentDescription = null
                    )
                } else
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.bookshelf_disabled),
                        contentDescription = null
                    )
            }

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                onClick = {
                    mainNavController.popBackStack()
                    mainNavController.navigate(MY_PAGE_ROUTE) {
                        restoreState = false
                    }
                }
            ) {
                if (currentRoute == MY_PAGE_ROUTE) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.mypage_enabled),
                        contentDescription = null
                    )
                } else
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.mypage_disabled),
                        contentDescription = null
                    )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomTabsPreView() {
    val appNavController = rememberNavController()
    val mainNavController = rememberNavController()
    AppTheme {
        BottomTabs(
            mainNavController = mainNavController,
            currentDestination = remember { mutableStateOf(HOME_ROUTE) },
        )
    }
}