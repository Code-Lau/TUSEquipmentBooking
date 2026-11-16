package com.example.tusequipmentbooking.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeScreen(
    onBrowseEquipment: () -> Unit,
    onBrowseAbout: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Welcome to TUS Equipment Booking",
            style = MaterialTheme.typography.headlineMedium
        )
        Text("Browse TUS equipment available for your projects.")
        Button(onClick = onBrowseEquipment) {
            Text("Browse equipment")
        }
        Text("Learn about what we do")
        Button(onClick = onBrowseAbout) {
            Text("About us")
        }
    }
}
