package com.example.tusequipmentbooking.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tusequipmentbooking.model.Equipment

@Composable
fun EquipmentCard(
    equipment: Equipment,
    categoryName: String,
    isBooked: Boolean,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onOpen,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                categoryName.uppercase(),
                style = MaterialTheme.typography.labelMedium
            )
            Text(equipment.name, style = MaterialTheme.typography.titleLarge)
            Text(equipment.description)
            Text(if (isBooked) "Currently booked" else "Available")
        }
    }
}
