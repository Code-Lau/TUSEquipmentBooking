package com.example.tusequipmentbooking.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tusequipmentbooking.model.Category
import com.example.tusequipmentbooking.model.Equipment
import com.example.tusequipmentbooking.ui.components.EquipmentCard

@Composable
fun ManageEquipmentScreen(
    equipment: List<Equipment>,
    categories: List<Category>,
    bookedEquipmentIds: List<Int>,
    accountEmail: String?,
    onLogin: () -> Unit,
    onLogout: () -> Unit,
    onRegister: () -> Unit,
    onMyBookings: () -> Unit,
    onEquipmentOpen: (Int) -> Unit
) {
    var selectedCategoryId by rememberSaveable { mutableStateOf<Int?>(null) }
    var filterMenuOpen by remember { mutableStateOf(false) }
    val filterLabel = categories.firstOrNull {
        it.id == selectedCategoryId
    }?.name ?: "All categories"
    val visibleEquipment = if (selectedCategoryId == null) equipment else
        equipment.filter { it.categoryId == selectedCategoryId }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { Text("Equipment", style = MaterialTheme.typography.headlineMedium) }
        item {
            if (accountEmail != null) {
                Text("Signed in as ${accountEmail ?: "user"}")
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onLogout) { Text("Logout") }
                    Button(onClick = onMyBookings) { Text("My bookings") }
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onLogin) { Text("Sign in") }
                    Button(onClick = onRegister) { Text("Register") }
                }
            }
        }
        item {
            Box {
                OutlinedButton(onClick = { filterMenuOpen = true }) {
                    Text("Category: $filterLabel")
                }
                DropdownMenu(expanded = filterMenuOpen,
                    onDismissRequest = { filterMenuOpen = false }) {
                    DropdownMenuItem(text = { Text("All categories") }, onClick = {
                        selectedCategoryId = null
                        filterMenuOpen = false
                    })
                    categories.forEach { category ->
                        DropdownMenuItem(text = { Text(category.name) }, onClick = {
                            selectedCategoryId = category.id
                            filterMenuOpen = false
                        })
                    }
                }
            }
        }
        if (visibleEquipment.isEmpty()) { item { Text("No equipment in this category.") } }
        items(visibleEquipment, key = { it.id }) { item ->
            val name = categories.firstOrNull {
                it.id == item.categoryId
            }?.name ?: "Uncategorised"
            EquipmentCard(equipment = item, categoryName = name,
                isBooked = item.id in bookedEquipmentIds,
                onOpen = { onEquipmentOpen(item.id) })
        }
    }
}
