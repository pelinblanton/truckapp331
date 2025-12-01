package com.example.truckapp331

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

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

    val deliveries = deliveryViewModel.allDeliveries.filter { it.isCompleted == showCompleted }

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

            // Toggle row (Pending / Past)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (showCompleted) "Review what you’ve finished." else "Select a stop to start or view.",
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
                    shape = RoundedCornerShape(20.dp)
                )
            }

            // Delivery list
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
                        DeliveryCard(
                            name = delivery.name,
                            quantity = delivery.quantity,
                            time = delivery.time,
                            isCompleted = delivery.isCompleted,
                            onClick = {
                                if (delivery.isCompleted) {
                                    navController.navigate("deliveryDetails/${delivery.id}")
                                } else {
                                    selectedDeliveryId = delivery.id
                                    showFirstDialog = true
                                }
                            }
                        )
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFA000)
                )
            ) {
                Text(
                    text = "📋 View Shift Summary",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Back to Dashboard (secondary)
            OutlinedButton(
                onClick = {
                    navController.navigate("dashboard") {
                        // adjust this route if you later centralize names
                        popUpTo("dashboard") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp)
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
                        navController.navigate("deliveryDetails/$selectedDeliveryId")
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
            text = { Text("Are you sure you want to start this delivery now?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        deliveryViewModel.startDelivery(selectedDeliveryId!!)
                        navController.navigate("deliveryDetails/$selectedDeliveryId")
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
            text = { Text("You haven't completed any deliveries yet.") },
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
            text = { Text("You have not completed all deliveries. Do you wish to continue to the shift summary?") },
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
private fun DeliveryCard(
    name: String,
    quantity: Int,
    time: String,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Quantity: $quantity",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF616161)
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(end = 4.dp),
                        tint = Color(0xFF757575)
                    )
                    Text(
                        text = time,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF757575)
                        )
                    )
                }
            }

            AssistChip(
                onClick = onClick,
                label = {
                    Text(
                        text = if (isCompleted) "Completed" else "Pending",
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                },
                shape = RoundedCornerShape(20.dp),
                colors = AssistChipDefaults.assistChipColors(
                    labelColor = if (isCompleted) Color(0xFF2E7D32) else Color(0xFF0277BD),
                    leadingIconContentColor = if (isCompleted) Color(0xFF2E7D32) else Color(0xFF0277BD),
                    containerColor = if (isCompleted) Color(0xFFE8F5E9) else Color(0xFFE1F5FE)
                )
            )
        }
    }
}
