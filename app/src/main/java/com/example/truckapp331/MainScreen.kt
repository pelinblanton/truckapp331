package com.example.truckapp331

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.truckapp33.LoginScreen

// Centralized route definitions for readability & safety
object Routes {
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val DELIVERIES = "deliveries"
    const val PAST_DELIVERIES = "pastDeliveries"
    const val DELIVERY_DETAILS = "deliveryDetails"
    const val SHIFT_SUMMARY = "shiftSummary"
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val deliveryViewModel: DeliveryViewModel = viewModel() // Shared instance across screens

    // Root surface to give a unified background and feel across the app
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            NavHost(
                navController = navController,
                startDestination = Routes.LOGIN
            ) {
                composable(Routes.LOGIN) {
                    // Login is independent of deliveries
                    LoginScreen(navController)
                }

                composable(Routes.DASHBOARD) {
                    DashboardScreen(navController)
                }

                composable(Routes.DELIVERIES) {
                    DeliveryListScreen(navController, deliveryViewModel)
                }

                composable(Routes.PAST_DELIVERIES) {
                    PastDeliveries(navController, deliveryViewModel)
                }

                composable(
                    route = "${Routes.DELIVERY_DETAILS}/{deliveryId}",
                    arguments = listOf(
                        navArgument("deliveryId") {
                            type = NavType.IntType
                        }
                    )
                ) { backStackEntry ->
                    val deliveryId = backStackEntry.arguments?.getInt("deliveryId")
                    if (deliveryId != null) {
                        CurrentDeliveryDetails(
                            navController = navController,
                            deliveryId = deliveryId,
                            deliveryViewModel = deliveryViewModel
                        )
                    }
                }

                composable(Routes.SHIFT_SUMMARY) {
                    ShiftSummary(navController, deliveryViewModel)
                }
            }
        }
    }
}
