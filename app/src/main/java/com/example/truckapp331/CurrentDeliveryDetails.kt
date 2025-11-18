package com.example.truckapp331

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.truckapp331.model.Delivery
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CurrentDeliveryDetails(
    navController: NavController,
    deliveryId: Int,
    deliveryViewModel: DeliveryViewModel = viewModel()
) {
    val delivery = deliveryViewModel.getDeliveryById(deliveryId)
    var showConfirmation by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf(TextFieldValue("")) }

    if (delivery == null) {
        Text("Delivery not found", modifier = Modifier.padding(16.dp))
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = delivery.name,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )

        Divider()

        Text("Name: ${delivery.name}")
        Text("Delivery Instructions: ${delivery.instructions}")
        Text("Quantity: ${delivery.quantity}")
        Text("Time: ${delivery.time}")

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Enter Notes Here") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedButton(
            onClick = { /* You can hook this up to a reporting screen later */ },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Report Delivery Issue")
        }

        Divider()

        Text("Customer Acknowledgement:")
        Text("Driver Acknowledgement:")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go Back to Delivery List")
        }

        if (!delivery.isCompleted) {
            Button(
                onClick = { showConfirmation = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Mark as Complete")
            }
        }
    }

    // Confirmation Dialog
    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            title = { Text("Confirm Completion") },
            text = { Text("Are you sure you want to mark this delivery as complete?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        deliveryViewModel.markDeliveryAsCompleted(delivery)
                        showConfirmation = false
                        navController.popBackStack() // Go back to the list
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}



