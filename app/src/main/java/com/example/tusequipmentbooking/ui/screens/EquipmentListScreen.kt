package com.example.tusequipmentbooking.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.tusequipmentbooking.model.Equipment
import com.example.tusequipmentbooking.model.Category
import com.example.tusequipmentbooking.ui.components.EquipmentCard

@Composable
fun HomeScreen(
    equipment: List<Equipment>, categories: List<Category>,
    onEquipmentOpen: (Int) -> Unit, onExplore: () -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Find the equipment you need",
                style = MaterialTheme.typography.headlineMedium)
            Button(onClick = onExplore) { Text("Browse all equipment") }
            Text("Featured equipment")
        }
        items(equipment.take(2), key = { it.id }) { item ->
            val name = categories.firstOrNull {
                it.id == item.categoryId
            }?.name ?: "Uncategorised"
            EquipmentCard(equipment = item, categoryName = name,
                onOpen = { onEquipmentOpen(item.id) })
        }
    }
}

@Composable
fun EquipmentScreen(
    equipment: List<Equipment>, categories: List<Category>,
    onEquipmentOpen: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { Text("All equipment",
            style = MaterialTheme.typography.headlineMedium) }
        items(equipment, key = { it.id }) { item ->
            val name = categories.firstOrNull {
                it.id == item.categoryId
            }?.name ?: "Uncategorised"
            EquipmentCard(equipment = item, categoryName = name,
                onOpen = { onEquipmentOpen(item.id) })
        }
    }
}

