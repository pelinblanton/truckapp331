package com.example.truckapp331

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun ShiftSummary(
    navController: NavController,
    deliveryViewModel: DeliveryViewModel = viewModel()
) {
    val completedCount = deliveryViewModel.getCompletedDeliveries().size
    val pendingCount = deliveryViewModel.getActiveDeliveries().size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("You have completed your deliveries!", style = MaterialTheme.typography.titleMedium)

        Text(
            "Shift Summary",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("✔ Completed Deliveries: $completedCount")
                Text("📦 Pending Deliveries: $pendingCount")
                Text("⏱ Shift Time: --") // Placeholder
                Text("🧭 Miles Traveled: --") // Placeholder
                Text("❌ Issues Encountered: --") // Placeholder
            }
        }


        Button(
            onClick = {
                navController.navigate("deliveries") {
                    popUpTo("shiftSummary") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("← Go Back to Delivery List")
        }

    }
}
