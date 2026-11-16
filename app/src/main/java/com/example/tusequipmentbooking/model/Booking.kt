package com.example.tusequipmentbooking.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "booking")
data class Booking(
    @PrimaryKey val id: Int,
    val name: String
    //Id, equipment id, user UID, duration in days, purpose and status
)
