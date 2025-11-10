package com.example.truckapp331

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.truckapp33.LoginScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val deliveryViewModel: DeliveryViewModel = viewModel() // Shared instance

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController) // Login doesn’t need deliveryViewModel
        }
        composable("dashboard") {
            DashboardScreen(navController)
        }
        composable("deliveries") {
            DeliveryListScreen(navController, deliveryViewModel)
        }
        composable("pastDeliveries") {
            PastDeliveries(navController, deliveryViewModel)
        }
        composable("deliveryDetails/{deliveryId}") { backStackEntry ->
            val deliveryId = backStackEntry.arguments?.getString("deliveryId")?.toIntOrNull()
            if (deliveryId != null) {
                CurrentDeliveryDetails(navController, deliveryId, deliveryViewModel)
            }
        }
        composable("shiftSummary") {
            ShiftSummary(navController, deliveryViewModel)
        }

    }
}










