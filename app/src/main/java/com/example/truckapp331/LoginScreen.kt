package com.example.truckapp33

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Text

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Login Page") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.LightGray
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "User ID:", fontSize = 16.sp, modifier = Modifier.align(Alignment.Start))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Text(text = "Password:", fontSize = 16.sp, modifier = Modifier.align(Alignment.Start))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            Button(
                onClick = {
                    if (username == "admin" && password == "password123") {
                        navController.navigate("dashboard")
                    } else {
                        Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // Green
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Login", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

        }
    }
}

