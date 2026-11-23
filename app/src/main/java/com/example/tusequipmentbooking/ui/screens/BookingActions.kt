package com.example.tusequipmentbooking.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tusequipmentbooking.model.Equipment

@Composable
fun BookingActions(equipment: Equipment, categoryName: String, signedIn: Boolean,
                   isBooked: Boolean, isBooking: Boolean,
                   message: String?, onLogin: () -> Unit, onRegister: () -> Unit,
                   onBook: (Int, String) -> Unit, onMyBookings: () -> Unit) {
    var showForm by rememberSaveable(equipment.id) { mutableStateOf(false) }
    var duration by rememberSaveable(equipment.id) { mutableStateOf(1) }
    var purpose by rememberSaveable(equipment.id) { mutableStateOf("") }
    LaunchedEffect(signedIn) { if (!signedIn) showForm = false }
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        if (!signedIn) {
            Button(onClick = onLogin) { Text("Sign in to book") }
            Button(onClick = onRegister) { Text("Register") }
        } else {
            Button(enabled = !isBooking && !isBooked,
                onClick = { showForm = true }) { Text("Book equipment") }
            Button(onClick = onMyBookings) { Text("My bookings") }
        }
        if (isBooked) Text("This equipment is currently booked.")
        message?.let { Text(it) }
    }
    if (signedIn && showForm) {
        AlertDialog(onDismissRequest = { showForm = false },
            title = { Text("Book ${equipment.name}") },
            text = {
                Column(Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Category: $categoryName")
                    Text("This booking is saved locally. It starts immediately.")
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(enabled = duration > 1,
                            onClick = { duration-- }) { Text("-") }
                        Text("$duration day(s)")
                        OutlinedButton(enabled = duration < 5,
                            onClick = { duration++ }) { Text("+") }
                    }
                    OutlinedTextField(purpose, { purpose = it },
                        label = { Text("Purpose") },
                        isError = purpose.isBlank(),
                        supportingText = {
                            if (purpose.isBlank()) Text("Enter the purpose of the booking")
                        })
                }
            },
            confirmButton = {
                TextButton(enabled = !isBooking && purpose.isNotBlank(),
                    onClick = {
                        onBook(duration, purpose.trim())
                        showForm = false
                        duration = 1
                        purpose = ""
                    }) { Text("Save booking locally") }
            },
            dismissButton = {
                TextButton(onClick = { showForm = false }) { Text("Cancel") }
            })
    }
}
