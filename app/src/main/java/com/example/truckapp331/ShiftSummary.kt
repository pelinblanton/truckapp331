package com.example.truckapp331

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShiftSummary(
    navController: NavController,
    deliveryViewModel: DeliveryViewModel = viewModel()
) {
    val completedCount = deliveryViewModel.getCompletedDeliveries().size
    val pendingCount = deliveryViewModel.getActiveDeliveries().size
    val total = completedCount + pendingCount

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Shift Summary",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0D47A1)
                )
            )
        },
        containerColor = Color(0xFFF4F6FB)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Congrats message
            Text(
                text = if (pendingCount == 0)
                    "Nice work! You’ve completed all assigned deliveries."
                else
                    "Good job! Here’s how your shift went.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF424242)
                ),
                modifier = Modifier.padding(top = 4.dp)
            )

            // Main summary card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Today’s Overview",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Row of key stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SummaryStat(
                            label = "Completed",
                            value = completedCount.toString()
                        )
                        SummaryStat(
                            label = "Pending",
                            value = pendingCount.toString()
                        )
                        SummaryStat(
                            label = "Total",
                            value = total.toString()
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Placeholder details (future data)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SummaryDetailRow(
                            label = "Shift Time",
                            value = "--"
                        )
                        SummaryDetailRow(
                            label = "Miles Traveled",
                            value = "--"
                        )
                        SummaryDetailRow(
                            label = "Issues Encountered",
                            value = "--"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Back to list button
            Button(
                onClick = {
                    navController.navigate("deliveries") {
                        popUpTo("shiftSummary") { inclusive = true }
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
                    text = "← Back to Delivery List",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

@Composable
private fun SummaryStat(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color(0xFF757575)
            )
        )
    }
}

@Composable
private fun SummaryDetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF616161)
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}
