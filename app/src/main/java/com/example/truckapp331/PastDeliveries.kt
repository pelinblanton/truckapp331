package com.example.truckapp331

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PastDeliveries(
    navController: NavController,
    deliveryViewModel: DeliveryViewModel
) {
    val pastDeliveries by remember(deliveryViewModel.allDeliveries) {
        derivedStateOf {
            deliveryViewModel.allDeliveries.filter { it.isCompleted }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("📦 Past Deliveries", style = MaterialTheme.typography.headlineMedium)

        if (pastDeliveries.isEmpty()) {
            Text("No deliveries completed yet.", modifier = Modifier.padding(top = 16.dp))
        } else {
            pastDeliveries.forEach { delivery ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            navController.navigate("deliveryDetails/${delivery.id}?started=true")
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(delivery.name, fontWeight = FontWeight.Bold)
                        Text("Quantity: ${delivery.quantity}")
                        Text("Time: ${delivery.time}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                navController.navigate("deliveries") {
                    popUpTo("pastDeliveries") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("← Back to Delivery List")
        }
    }
}
