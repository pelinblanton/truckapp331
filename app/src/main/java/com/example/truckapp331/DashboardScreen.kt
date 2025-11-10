package com.example.truckapp331

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    val date = remember { SimpleDateFormat("M-dd-yy", Locale.getDefault()).format(Date()) }
    val time = remember { SimpleDateFormat("h:mma", Locale.getDefault()).format(Date()).lowercase() }
    val deliveries = 3

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Today", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFDDDDDD) // light gray
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Hello, User! Field
            OutlinedTextField(
                value = "Hello, User!",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = Color.Black,
                    focusedBorderColor = Color.DarkGray,
                    unfocusedBorderColor = Color.DarkGray
                )
            )

            // Info Box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFBBDEFB), RoundedCornerShape(4.dp)) // light blue
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoRow(Icons.Default.CalendarToday, "Today is $date")
                InfoRow(Icons.Default.AccessTime, time)
                InfoRow(Icons.Default.LocalShipping, "Number of Deliveries: $deliveries")
            }

            // View My Route Button
            Button(
                onClick = {
                    navController.navigate("deliveries")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text("View My Route!")
            }
        }
    }
}

@Composable
fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier
                .size(20.dp)
                .padding(end = 12.dp)
        )
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}
