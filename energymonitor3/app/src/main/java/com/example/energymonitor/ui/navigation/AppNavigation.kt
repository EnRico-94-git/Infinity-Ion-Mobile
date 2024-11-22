package com.example.energymonitor.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.energymonitor.ui.screens.*

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("add") {
            AddConsumptionScreen(navController)
        }
        composable("history") {
            ConsumptionHistoryScreen(navController)
        }
        composable("edit_consumption_screen/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            EditConsumptionScreen(navController, id)
        }
    }
}
