package project.side.ikdaman.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
import project.side.ikdaman.core.navigation.SEARCH_INFO_ROUTE
import project.side.ikdaman.core.navigation.TUTORIAL_ROUTE
import project.side.ikdaman.core.ui.AppTheme
import project.side.ikdaman.feature.barcode.BarcodeScreen
import project.side.ikdaman.feature.bookedit.BookEditScreen
import project.side.ikdaman.feature.detail.BookDetailScreen
import project.side.ikdaman.feature.login.LoginScreen
import project.side.ikdaman.feature.addbook.AddBookScreen
import project.side.ikdaman.feature.tutorial.TutorialScreen

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
                    slideComposable(TUTORIAL_ROUTE) {
                        TutorialScreen(navController)
                    }
                    slideComposable(MAIN_ROUTE) {
                        MainScreen(navController)
                    }
                    slideComposable(BARCODE_ROUTE) {
                        BarcodeScreen(navController)
                    }
                    slideComposable(
                        route = "$SEARCH_INFO_ROUTE/{isbn}",
                        arguments = listOf(
                            navArgument("isbn") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val isbn = backStackEntry.arguments?.getString("isbn") ?: return@slideComposable
                        AddBookScreen(isbn = isbn, navController = navController)
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
