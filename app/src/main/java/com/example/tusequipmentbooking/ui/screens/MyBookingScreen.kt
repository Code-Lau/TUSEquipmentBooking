package com.example.tusequipmentbooking.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tusequipmentbooking.model.BookingStatus
import com.example.tusequipmentbooking.model.BookingSummary

@Composable
fun MyBookingsScreen(signedIn: Boolean, bookings: List<BookingSummary>,
                     message: String?, isBusy: Boolean, onCancelBooking: (Int) -> Unit,
                     onLogin: () -> Unit, onBack: () -> Unit, onBrowseEquipment: () -> Unit) {
    var bookingToCancelId by rememberSaveable { mutableStateOf<Int?>(null) }
    LazyColumn(contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Button(onClick = onBack) { Text("Back") }
            Text("My bookings", style = MaterialTheme.typography.headlineMedium)

        }
        if (!signedIn) {
            item { Button(onClick = onLogin) { Text("Sign in") } }
        } else {
            if (bookings.isEmpty()) item {
                Text("No bookings yet. Browse the equipment list and book an item.")
                Button(onClick = onBrowseEquipment) {
                    Text("Browse equipment")
                }
            }
            items(bookings, key = { it.id }) { booking ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(booking.name, style = MaterialTheme.typography.titleMedium)
                        Text("Duration: ${booking.durationDays} day(s)")
                        Text("Purpose: ${booking.purpose}")
                        Text("Status: ${booking.status}")
                        if (booking.status == BookingStatus.Active.name) {
                            Button(enabled = !isBusy,
                                onClick = { bookingToCancelId = booking.id }) {
                                Text("Cancel booking")
                            }
                        }
                    }
                }
            }
        }
        message?.let { text -> item { Text(text) } }
    }
    bookings.firstOrNull { it.id == bookingToCancelId }?.let { booking ->
        AlertDialog(onDismissRequest = { bookingToCancelId = null },
            title = { Text("Cancel booking?") },
            text = { Text("Are you sure you want to cancel your booking of ${booking.name}?") },
            confirmButton = {
                TextButton(enabled = !isBusy, onClick = {
                    onCancelBooking(booking.id)
                    bookingToCancelId = null
                }) { Text("Cancel booking") }
            },
            dismissButton = {
                TextButton(onClick = { bookingToCancelId = null }) { Text("Keep booking") }
            })
    }
}
