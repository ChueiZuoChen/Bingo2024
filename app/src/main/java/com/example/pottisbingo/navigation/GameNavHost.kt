package com.example.pottisbingo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pottisbingo.ui.screens.LoginScreen

@Composable
fun GameNavHost(
    navHostController: NavHostController,
) {
    NavHost(navController = navHostController, startDestination = NavRoutes.MainScreen) {
        composable(NavRoutes.MainScreen) {
            LoginScreen(message = "123test")
        }
    }
}