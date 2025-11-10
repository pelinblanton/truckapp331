package com.example.truckapp331

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.truckapp331.model.Delivery

@Composable
fun DeliveryListScreen(
    navController: NavController,
    deliveryViewModel: DeliveryViewModel = viewModel()
) {
    var showCompleted by remember { mutableStateOf(false) }

    // 💡 Auto-updates when `markDeliveryAsCompleted()` is called
    val deliveries = deliveryViewModel.allDeliveries.filter { it.isCompleted == showCompleted }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(
            text = if (showCompleted) "Past Deliveries" else "Deliveries to Complete",
            style = MaterialTheme.typography.headlineMedium
        )

        Button(
            onClick = { showCompleted = !showCompleted },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Text(if (showCompleted) "View Pending Deliveries" else "View Past Deliveries")
        }

        deliveries.forEach { delivery ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        navController.navigate("deliveryDetails/${delivery.id}")
                    }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(delivery.name, fontWeight = FontWeight.Bold)
                    Text("Quantity: ${delivery.quantity}")
                    Text("Time: ${delivery.time}")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { navController.navigate("shiftSummary") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📋 View Shift Summary")
        }

        Button(
            onClick = {
                navController.navigate("dashboard") {
                    popUpTo("deliveryList") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("← Back to Dashboard")
        }
    }
}


