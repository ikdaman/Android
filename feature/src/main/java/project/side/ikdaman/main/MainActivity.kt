package project.side.ikdaman.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import project.side.ikdaman.core.navigation.ADD_BOOK_RECORD
import project.side.ikdaman.core.navigation.ADD_BOOK_ROUTE
import project.side.ikdaman.core.navigation.BARCODE_ROUTE
import project.side.ikdaman.core.navigation.BOOK_DETAIL_ROUTE
import project.side.ikdaman.core.navigation.EnterToLeftTransition
import project.side.ikdaman.core.navigation.EnterToRightTransition
import project.side.ikdaman.core.navigation.ExitToLeftTransition
import project.side.ikdaman.core.navigation.ExitToRightTransition
import project.side.ikdaman.core.navigation.LOGIN_ROUTE
import project.side.ikdaman.core.navigation.MAIN_ROUTE
import project.side.ikdaman.core.navigation.SPLASH_ROUTE
import project.side.ikdaman.core.navigation.USERINFO_ROUTE
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.domain.model.RecordType
import project.side.ikdaman.feature.add_notes.AddRecordScreen
import project.side.ikdaman.feature.addbook.AddBookScreen
import project.side.ikdaman.feature.barcode.BarcodeScreen
import project.side.ikdaman.feature.detail.BookDetailScreen
import project.side.ikdaman.feature.login.LoginScreen
import project.side.ikdaman.feature.mypage.UserInfoScreen
import project.side.ikdaman.feature.splash.SplashScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = SPLASH_ROUTE) {
                    slideComposable(SPLASH_ROUTE) {
                        SplashScreen(navController)
                    }
                    slideComposable(LOGIN_ROUTE) {
                        LoginScreen(navController)
                    }
                    slideComposable(MAIN_ROUTE) {
                        MainScreen(navController) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                if (ContextCompat.checkSelfPermission(
                                        this@MainActivity,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    ActivityCompat.requestPermissions(
                                        this@MainActivity,
                                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                                        100
                                    )
                                }
                            }
                        }
                    }
                    slideComposable(
                        "${BARCODE_ROUTE}/{fromWhere}",
                        arguments = listOf(
                            navArgument("fromWhere") { type = NavType.StringType; }
                        )
                    ) { backStackEntry ->
                        val fromWhere = backStackEntry.arguments?.getString("fromWhere") ?: ""
                        BarcodeScreen(navController = navController, fromWhere = fromWhere)
                    }
                    slideComposable(
                        route = "$ADD_BOOK_ROUTE/{isbn}",
                        arguments = listOf(
                            navArgument("isbn") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val isbn =
                            backStackEntry.arguments?.getString("isbn") ?: return@slideComposable
                        AddBookScreen(isbn = isbn, navController = navController)
                    }
                    slideComposable(
                        "$BOOK_DETAIL_ROUTE/{bookId}/{isShowFirstLog}",
                        arguments = listOf(
                            navArgument("bookId") { type = NavType.StringType },
                            navArgument("isShowFirstLog") {
                                type = NavType.BoolType
                                defaultValue = false
                            }
                        )
                    ) {
                        val bookId = it.arguments?.getString("bookId") ?: return@slideComposable
                        val isShowFirstLog = it.arguments?.getBoolean("isShowFirstLog") ?: false
                        BookDetailScreen(navController, bookId = bookId, isShowFirstLog)
                    }
                    slideComposable(
                        "$ADD_BOOK_RECORD/{recordType}/{bookId}?isShowFirstLog={isShowFirstLog}",
                        arguments = listOf(
                            navArgument("recordType") { type = NavType.StringType },
                            navArgument("bookId") { type = NavType.StringType },
                            navArgument("isShowFirstLog") {
                                type = NavType.BoolType
                                defaultValue = false
                            }
                        )
                    ) {
                        val recordType = it.arguments?.getString("recordType") ?: RecordType.THINK
                        val bookId = it.arguments?.getString("bookId") ?: return@slideComposable
                        val isShowFirstLog = it.arguments?.getBoolean("isShowFirstLog") ?: false

                        AddRecordScreen(
                            navController,
                            isShowFirstLog = isShowFirstLog,
                            recordType = recordType,
                            bookId = bookId
                        )
                    }
                    slideComposable(USERINFO_ROUTE) {
                        UserInfoScreen(navController)
                    }
                }
            }
        }
    }

    private fun NavGraphBuilder.slideComposable(
        route: String,
        arguments: List<NamedNavArgument> = emptyList(),
        content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
    ) {
        composable(
            route,
            arguments,
            enterTransition = EnterToLeftTransition(),
            popEnterTransition = EnterToRightTransition(),
            exitTransition = ExitToLeftTransition(),
            popExitTransition = ExitToRightTransition(),
            content = content
        )
    }
}
