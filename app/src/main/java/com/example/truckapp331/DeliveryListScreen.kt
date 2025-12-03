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
    var selectedDeliveryId by remember { mutableStateOf<Int?>(null) }
    var showFirstDialog by remember { mutableStateOf(false) }
    var showSecondDialog by remember { mutableStateOf(false) }
    var showCompleted by remember { mutableStateOf(false) }

    var showNoneCompletedDialog by remember { mutableStateOf(false) }
    var showIncompleteDialog by remember { mutableStateOf(false) }

    val deliveries by remember(showCompleted, deliveryViewModel.allDeliveries) {
        derivedStateOf {
            deliveryViewModel.allDeliveries.filter { it.isCompleted == showCompleted }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
                        selectedDeliveryId = delivery.id

                        if (delivery.isCompleted || delivery.hasStarted) {
                            navController.navigate("deliveryDetails/${delivery.id}")
                        } else {
                            showFirstDialog = true
                        }
                    }

            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(delivery.name, fontWeight = FontWeight.Bold)
                    Text("Quantity: ${delivery.quantity}")
                    Text("Time: ${delivery.time}")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Shift Summary Button
        Button(
            onClick = {
                val completed = deliveryViewModel.getCompletedCount()
                val total = deliveryViewModel.getTotalCount()

                when {
                    completed == 0 -> showNoneCompletedDialog = true
                    completed < total -> showIncompleteDialog = true
                    else -> navController.navigate("shiftSummary")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📋 View Shift Summary")
        }

        Spacer(modifier = Modifier.weight(1f))

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

    // First Dialog: Start or View
    if (showFirstDialog && selectedDeliveryId != null) {
        AlertDialog(
            onDismissRequest = { showFirstDialog = false },
            title = { Text("Start or View Delivery?") },
            text = { Text("Would you like to start this delivery or just view its details?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showFirstDialog = false
                        showSecondDialog = true
                    }
                ) {
                    Text("Start Delivery")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        navController.navigate("deliveryDetails/$selectedDeliveryId?started=false")
                        showFirstDialog = false
                    }
                ) {
                    Text("View Only")
                }
            }
        )
    }

    // Second Dialog: Confirm Start
    if (showSecondDialog && selectedDeliveryId != null) {
        AlertDialog(
            onDismissRequest = { showSecondDialog = false },
            title = { Text("Start Delivery?") },
            text = { Text("Are you sure you want to start this delivery?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        deliveryViewModel.startDelivery(selectedDeliveryId!!)
                        navController.navigate("deliveryDetails/$selectedDeliveryId?started=true")
                        showSecondDialog = false
                    }
                ) {
                    Text("Yes, Start")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSecondDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // None completed dialog
    if (showNoneCompletedDialog) {
        AlertDialog(
            onDismissRequest = { showNoneCompletedDialog = false },
            title = { Text("No Deliveries Completed") },
            text = { Text("You haven't completed any deliveries.") },
            confirmButton = {
                TextButton(onClick = { showNoneCompletedDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    //Incomplete deliveries dialog
    if (showIncompleteDialog) {
        AlertDialog(
            onDismissRequest = { showIncompleteDialog = false },
            title = { Text("Incomplete Deliveries") },
            text = { Text("You have not completed all deliveries. Do you wish to continue?") },
            confirmButton = {
                TextButton(onClick = {
                    showIncompleteDialog = false
                    navController.navigate("shiftSummary")
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showIncompleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
