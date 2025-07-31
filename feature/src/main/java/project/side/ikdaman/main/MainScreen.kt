package project.side.ikdaman.main

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import project.side.ikdaman.app.feature.R
import project.side.ikdaman.core.navigation.BARCODE_ROUTE
import project.side.ikdaman.core.navigation.BOOKSHELF_ROUTE
import project.side.ikdaman.core.navigation.FromWhere
import project.side.ikdaman.core.navigation.HOME_ROUTE
import project.side.ikdaman.core.navigation.MY_PAGE_ROUTE
import project.side.ikdaman.core.navigation.SEARCH_ROUTE
import project.side.ikdaman.core.ui.AppText
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.core.view.CustomModalBottomSheet
import project.side.ikdaman.feature.bookshelf.BookShelfTab
import project.side.ikdaman.feature.home.HomeTab
import project.side.ikdaman.feature.mypage.MyPageTab
import project.side.ikdaman.feature.search.SearchTab

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(appNavController: NavHostController, onNotificationPermissionCheck: () -> Unit = {}) {
    val mainNavController = rememberNavController()
    val currentDestination = remember { mutableStateOf(HOME_ROUTE) }
    val addBookDialogState = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            floatingActionButton = {
                if (currentDestination.value == HOME_ROUTE) {
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
                }
            },
            bottomBar = {
                BottomTabs(
                    mainNavController = mainNavController,
                    currentDestination = currentDestination,
                )
            }
        ) {
            NavHost(
                navController = mainNavController,
                startDestination = HOME_ROUTE,
            ) {
                composable(HOME_ROUTE) {
                    currentDestination.value = HOME_ROUTE
                    HomeTab(appNavController, mainNavController)
                }
                composable(SEARCH_ROUTE) {
                    currentDestination.value = SEARCH_ROUTE
                    SearchTab(appNavController, mainNavController)
                }
                composable(BOOKSHELF_ROUTE) {
                    currentDestination.value = BOOKSHELF_ROUTE
                    BookShelfTab(appNavController)
                }
                composable(MY_PAGE_ROUTE) {
                    currentDestination.value = MY_PAGE_ROUTE
                    MyPageTab(appNavController, onNotificationPermissionCheck)
                }
            }
        }

        val onDismissDialog = remember { { addBookDialogState.value = false } }
        if (addBookDialogState.value) {
            CustomModalBottomSheet(
                onDismiss = onDismissDialog,
                content = { modifier ->
                    Column(
                        modifier
                            .padding(horizontal = 20.dp, vertical = 25.dp)
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppText(
                                text = "책 등록하기",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    lineHeight = 26.sp
                                )
                            )
                            IconButton(
                                modifier = Modifier.size(26.dp),
                                onClick = {
                                    onDismissDialog()
                                }
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "닫기")
                            }
                        }

                        Spacer(Modifier.height(15.dp))

                        AppText(
                            text = "검색해서 등록",
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                lineHeight = 26.sp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    mainNavController.navigate(SEARCH_ROUTE)
                                    onDismissDialog()
                                }
                        )
                        Spacer(Modifier.height(15.dp))

                        AppText(
                            text = "바코드로 등록",
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                lineHeight = 26.sp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    appNavController.navigate("${BARCODE_ROUTE}/${FromWhere.FROM_MAIN}")
                                    onDismissDialog()
                                }
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun BottomTabs(
    mainNavController: NavHostController,
    currentDestination: MutableState<String>,
) {
    val currentRoute = currentDestination.value
    Column {
        HorizontalDivider(thickness = 1.dp, color = Color(0xFFDDDDDD))
        Row(
            Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(8.dp)
                .navigationBarsPadding(),
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
                } else {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.home_disabled),
                        contentDescription = null
                    )
                }
            }

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                elevation = null,
                onClick = {
                    mainNavController.popBackStack()
                    mainNavController.navigate(SEARCH_ROUTE)
                }
            ) {
                if (currentRoute == SEARCH_ROUTE) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.search_enabled),
                        contentDescription = null
                    )
                } else {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.search_disabled),
                        contentDescription = null
                    )
                }
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
                } else {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.bookshelf_disabled),
                        contentDescription = null
                    )
                }
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
                } else {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.mypage_disabled),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomTabsPreView() {
    val mainNavController = rememberNavController()
    AppTheme {
        BottomTabs(
            mainNavController = mainNavController,
            currentDestination = remember { mutableStateOf(HOME_ROUTE) },
        )
    }
}
