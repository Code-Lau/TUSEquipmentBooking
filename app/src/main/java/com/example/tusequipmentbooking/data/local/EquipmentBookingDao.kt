package com.example.tusequipmentbooking.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tusequipmentbooking.model.Booking
import com.example.tusequipmentbooking.model.BookingSummary
import com.example.tusequipmentbooking.model.Category
import com.example.tusequipmentbooking.model.Equipment
import kotlinx.coroutines.flow.Flow

@Dao
interface EquipmentBookingDao {
    @Query("SELECT * FROM categories ORDER BY id")
    fun observeCategories(): Flow<List<Category>>
    @Query("SELECT * FROM equipment ORDER BY id")
    fun observeEquipment(): Flow<List<Equipment>>
    @Query("SELECT COUNT(*) FROM categories")
    suspend fun categoryCount(): Int
    @Query("SELECT COUNT(*) FROM equipment")
    suspend fun equipmentCount(): Int
    @Insert
    suspend fun insertCategories(categories: List<Category>)
    @Insert
    suspend fun insertEquipment(equipment: List<Equipment>)
    @Insert
    suspend fun insertBooking(booking: Booking): Long
    @Query("SELECT equipmentId FROM bookings WHERE status = 'Active'")
    fun observeBookedEquipmentIds(): Flow<List<Int>>

    @Query("SELECT COUNT(*) FROM bookings WHERE equipmentId = :equipmentId AND status = 'Active'")
    suspend fun activeBookingCount(equipmentId: Int): Int

    @Query("""
    UPDATE bookings SET status = 'Cancelled'
    WHERE id = :bookingId AND userUid = :userUid AND status = 'Active'
""")
    suspend fun cancelBooking(bookingId: Int, userUid: String): Int

    @Query("""
    SELECT b.id, b.equipmentId, e.name, b.durationDays, b.purpose, b.status
    FROM bookings AS b JOIN equipment AS e ON e.id = b.equipmentId
    WHERE b.userUid = :userUid ORDER BY b.id DESC
""")
    fun observeBookings(userUid: String): Flow<List<BookingSummary>>
}
