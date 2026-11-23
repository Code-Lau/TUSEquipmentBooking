package com.example.tusequipmentbooking.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tusequipmentbooking.model.Equipment

@Composable
fun EquipmentDetailsScreen(
    equipment: Equipment,
    categoryName: String,
    isBooked: Boolean,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(onClick = onBack) { Text("Back") }
        Text(categoryName, style = MaterialTheme.typography.labelLarge)
        Text(equipment.name, style = MaterialTheme.typography.headlineMedium)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Category: $categoryName")
                Text(
                    if (isBooked) "Availability: Currently booked"
                    else "Availability: Available"
                )
            }
        }
        Text("About this equipment", style = MaterialTheme.typography.titleLarge)
        Text(equipment.description)
    }
}
