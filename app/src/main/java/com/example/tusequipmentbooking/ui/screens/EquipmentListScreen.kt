package com.example.tusequipmentbooking.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tusequipmentbooking.model.Category
import com.example.tusequipmentbooking.model.Equipment
import com.example.tusequipmentbooking.ui.components.EquipmentCard

@Composable
fun HomeScreen(
    equipment: List<Equipment>,
    categories: List<Category>,
    bookedEquipmentIds: List<Int>,
    onEquipmentOpen: (Int) -> Unit,
    onExplore: () -> Unit,
    onBack: () -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Button(onClick = onBack) { Text("Back") }
        }
        item {
            Text("Borrow campus equipment", style = MaterialTheme.typography.headlineMedium)
            Text("Cameras, audio and computing kit for TUS students and staff.")
        }
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Book today", style = MaterialTheme.typography.titleLarge)
                    Text("Explore equipment and book it for one to five days.")
                    Button(onClick = onExplore) { Text("Browse all equipment") }
                }
            }
        }
        item { Text("Featured equipment", style = MaterialTheme.typography.titleLarge) }

        items(equipment.take(2), key = { it.id }) { item ->
            val name = categories.firstOrNull {
                it.id == item.categoryId
            }?.name ?: "Uncategorised"
            EquipmentCard(equipment = item, categoryName = name,
                isBooked = item.id in bookedEquipmentIds,
                onOpen = { onEquipmentOpen(item.id) })
        }
    }
}
