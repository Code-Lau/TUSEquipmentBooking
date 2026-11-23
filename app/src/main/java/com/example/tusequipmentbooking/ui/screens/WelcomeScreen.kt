package com.example.tusequipmentbooking.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeScreen(
    signedIn: Boolean,
    onBrowseEquipment: () -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onBrowseAbout: () -> Unit,
    onHelp: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Welcome to TUS Equipment Booking",
            style = MaterialTheme.typography.headlineMedium
        )
        Text("Browse and book equipment")
        Button(onClick = onBrowseEquipment) {
            Text("Browse equipment")
        }
        if (!signedIn) {
            Text("Log in or register to book equipment.")
            Button(onClick = onLogin) {
                Text("Login")
            }
            Button(onClick = onRegister) {
                Text("Register")
            }
        }
        Text("Learn about the equipment booking service.")
        Button(onClick = onBrowseAbout) {
            Text("About us")
        }
        Text("Need assistance with booking or cancelling equipment?")
        Button(onClick = onHelp) {
            Text("Help & Instructions")
        }
    }
}
