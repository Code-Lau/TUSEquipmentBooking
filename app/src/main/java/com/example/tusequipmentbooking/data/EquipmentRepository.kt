package com.example.tusequipmentbooking.data

import androidx.room.withTransaction
import com.example.tusequipmentbooking.data.local.EquipmentBookingDatabase
import com.example.tusequipmentbooking.model.Booking

class EquipmentRepository(private val database: EquipmentBookingDatabase) {
    private val dao = database.equipmentBookingDao()
    val equipment = dao.observeEquipment()
    val categories = dao.observeCategories()
    val bookedEquipmentIds = dao.observeBookedEquipmentIds()

    suspend fun seedIfEmpty() {
        database.withTransaction {
            if (dao.categoryCount() == 0 && dao.equipmentCount() == 0) {
                dao.insertCategories(sampleCategories)
                dao.insertEquipment(sampleEquipments)
            }
        }
    }

    fun observeBookings(userUid: String) = dao.observeBookings(userUid)

    suspend fun bookEquipment(
        equipmentId: Int, userUid: String, durationDays: Int, purpose: String
    ): Long {
        require(userUid.isNotBlank()) { "Login required" }
        require(durationDays in 1..5) { "Choose a duration of 1 to 5 days" }
        require(purpose.isNotBlank()) { "Enter a purpose for the booking" }
        return database.withTransaction {
            check(dao.activeBookingCount(equipmentId) == 0) {
                "This equipment is already booked"
            }
            dao.insertBooking(
                Booking(
                    equipmentId = equipmentId, userUid = userUid,
                    durationDays = durationDays, purpose = purpose.trim()
                )
            )
        }
    }

    suspend fun cancelBooking(bookingId: Int, userUid: String) {
        require(userUid.isNotBlank()) { "Login required" }
        check(dao.cancelBooking(bookingId, userUid) == 1) {
            "Booking is no longer active"
        }
    }
}
