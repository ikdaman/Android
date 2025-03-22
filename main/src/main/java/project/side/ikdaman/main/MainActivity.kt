package project.side.ikdaman.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import project.side.ikdaman.core.navigation.BARCODE_ROUTE
import project.side.ikdaman.core.navigation.BOOK_DETAIL_ROUTE
import project.side.ikdaman.core.navigation.BOOK_EDIT_ROUTE
import project.side.ikdaman.core.navigation.EnterToLeftTransition
import project.side.ikdaman.core.navigation.EnterToRightTransition
import project.side.ikdaman.core.navigation.ExitToLeftTransition
import project.side.ikdaman.core.navigation.ExitToRightTransition
import project.side.ikdaman.core.navigation.LOGIN_ROUTE
import project.side.ikdaman.core.navigation.MAIN_ROUTE
import project.side.ikdaman.core.navigation.SEARCH_ROUTE
import project.side.ikdaman.core.navigation.SIGNUP_ROUTE
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.feature.barcode.BarcodeScreen
import project.side.ikdaman.feature.bookedit.BookEditScreen
import project.side.ikdaman.feature.detail.BookDetailScreen
import project.side.ikdaman.feature.login.LoginScreen
import project.side.ikdaman.feature.search.SearchScreen
import project.side.ikdaman.feature.signup.SignUpScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = LOGIN_ROUTE) {
                    slideComposable(LOGIN_ROUTE) {
                        LoginScreen(navController)
                    }
                    slideComposable(SIGNUP_ROUTE) {
                        SignUpScreen(navController)
                    }
                    slideComposable(MAIN_ROUTE) {
                        MainScreen(navController)
                    }
                    slideComposable(SEARCH_ROUTE) {
                        SearchScreen(navController, hiltViewModel())
                    }
                    slideComposable(BARCODE_ROUTE) {
                        BarcodeScreen(navController)
                    }
                    slideComposable(BOOK_EDIT_ROUTE) {
                        BookEditScreen(navController)
                    }
                    slideComposable(BOOK_DETAIL_ROUTE) {
                        BookDetailScreen(navController)
                    }
                }
            }
        }
    }

    private fun NavGraphBuilder.slideComposable(
        route: String,
        content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
    ) {
        composable(
            route,
            enterTransition = EnterToLeftTransition(),
            popEnterTransition = EnterToRightTransition(),
            exitTransition = ExitToLeftTransition(),
            popExitTransition = ExitToRightTransition(),
            content = content
        )
    }
}