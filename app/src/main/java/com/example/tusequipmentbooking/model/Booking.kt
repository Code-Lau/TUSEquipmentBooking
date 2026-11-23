package com.example.tusequipmentbooking.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class BookingStatus { Active, Cancelled }

@Entity(
    tableName = "bookings",
    foreignKeys = [ForeignKey(
        entity = Equipment::class,
        parentColumns = ["id"], childColumns = ["equipmentId"],
        onDelete = ForeignKey.RESTRICT
    )],
    indices = [Index(value = ["equipmentId"]), Index(value = ["userUid"])]
)
data class Booking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val equipmentId: Int,
    val userUid: String,
    val durationDays: Int,
    val purpose: String,
    val status: String = BookingStatus.Active.name
)

// Result of a JOIN, not a separate Room table.
data class BookingSummary(
    val id: Int,
    val equipmentId: Int,
    val name: String,
    val durationDays: Int,
    val purpose: String,
    val status: String
)
