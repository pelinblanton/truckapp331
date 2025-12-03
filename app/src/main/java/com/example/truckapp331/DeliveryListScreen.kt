package com.example.truckapp331

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.truckapp331.model.Delivery

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (showCompleted) "Past Deliveries" else "Today’s Deliveries",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0D47A1),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF4F6FB)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {

            // Helper text + toggle chip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (showCompleted)
                        "Review your completed deliveries."
                    else
                        "Tap a delivery to start or view details.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF616161)
                    ),
                    modifier = Modifier.weight(1f)
                )

                AssistChip(
                    onClick = { showCompleted = !showCompleted },
                    label = {
                        Text(
                            text = if (showCompleted) "View Pending" else "View Past",
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    shape = MaterialTheme.shapes.small
                )
            }

            // Delivery list section
            if (deliveries.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (showCompleted)
                            "No past deliveries to show yet."
                        else
                            "No deliveries assigned right now.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF9E9E9E)
                        )
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(deliveries) { delivery ->
                        DeliveryListItemCard(
                            delivery = delivery,
                            onClick = {
                                selectedDeliveryId = delivery.id

                                if (delivery.isCompleted || delivery.hasStarted) {
                                    navController.navigate("deliveryDetails/${delivery.id}?started=true")
                                } else {
                                    showFirstDialog = true
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Shift Summary Button (primary CTA)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFA000)
                )
            ) {
                Text(
                    text = "📋 View Shift Summary",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Back to dashboard (secondary)
            OutlinedButton(
                onClick = {
                    navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("← Back to Dashboard")
            }
        }
    }

    // First Dialog: Start or View
    if (showFirstDialog && selectedDeliveryId != null) {
        AlertDialog(
            onDismissRequest = { showFirstDialog = false },
            title = { Text("Open Delivery") },
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

    // Incomplete deliveries dialog
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

@Composable
private fun DeliveryListItemCard(
    delivery: Delivery,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = delivery.name,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Quantity: ${delivery.quantity}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Time: ${delivery.time}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            val statusLabel = when {
                delivery.isCompleted -> "Completed"
                delivery.hasStarted -> "In Progress"
                else -> "Pending"
            }

            Text(
                text = statusLabel,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = when {
                        delivery.isCompleted -> Color(0xFF2E7D32) // green-ish
                        delivery.hasStarted -> Color(0xFF0277BD) // blue-ish
                        else -> Color(0xFF757575) // neutral
                    }
                )
            )
        }
    }
}
