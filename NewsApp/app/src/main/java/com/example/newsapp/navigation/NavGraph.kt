package com.example.newsapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsapp.ui.screens.DetailScreen
import com.example.newsapp.ui.screens.HomeScreen
import com.example.newsapp.viewmodel.NewsUiState
import com.example.newsapp.viewmodel.NewsViewModel

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val viewModel: NewsViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onDetailClick = { articleIndex ->
                    navController.navigate("detail/$articleIndex")
                }
            )
        }

        composable(
            route = "detail/{articleIndex}",
            arguments = listOf(
                navArgument("articleIndex") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val articleIndex = backStackEntry.arguments?.getInt("articleIndex") ?: 0
            val state by viewModel.uiState.collectAsState()

            if (state is NewsUiState.Success) {
                val articles = (state as NewsUiState.Success).articles
                if (articleIndex in articles.indices) {
                    DetailScreen(
                        article = articles[articleIndex],
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
