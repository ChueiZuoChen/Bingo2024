package com.example.pottisbingo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pottisbingo.ui.screens.GameScreen
import com.example.pottisbingo.ui.screens.MainScreen
import com.example.pottisbingo.ui.screens.MainScreenViewModel

@Composable
fun GameNavHost(
    navHostController: NavHostController,
    viewModel: MainScreenViewModel,
) {
    NavHost(navController = navHostController, startDestination = NavRoutes.MainScreen) {
        composable(NavRoutes.MainScreen) {
            MainScreen(viewModel = viewModel) {
                navHostController.navigate(NavRoutes.GameScreen)
            }
        }
        composable(NavRoutes.GameScreen) {
            GameScreen(viewModel = viewModel) {
                navHostController.navigate(NavRoutes.MainScreen)
            }
        }
    }
}