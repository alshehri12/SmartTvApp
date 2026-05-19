package com.example.playimdb.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.playimdb.ui.detail.DetailScreen
import com.example.playimdb.ui.home.HomeScreen
import com.example.playimdb.ui.player.PlayerScreen
import com.example.playimdb.ui.search.SearchScreen

private object Routes {
    const val HOME = "home"
    const val SEARCH = "search"
    const val DETAIL = "detail/{movieId}"
    const val PLAYER = "player/{imdbId}"

    fun detail(movieId: Int) = "detail/$movieId"
    fun player(imdbId: String) = "player/$imdbId"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Routes.detail(movieId))
                },
                onSearchClick = {
                    navController.navigate(Routes.SEARCH)
                }
            )
        }

        composable(Routes.SEARCH) {
            SearchScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Routes.detail(movieId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
            DetailScreen(
                movieId = movieId,
                onPlayClick = { imdbId ->
                    navController.navigate(Routes.player(imdbId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.PLAYER,
            arguments = listOf(navArgument("imdbId") { type = NavType.StringType })
        ) { backStackEntry ->
            val imdbId = backStackEntry.arguments?.getString("imdbId") ?: return@composable
            PlayerScreen(
                imdbId = imdbId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
