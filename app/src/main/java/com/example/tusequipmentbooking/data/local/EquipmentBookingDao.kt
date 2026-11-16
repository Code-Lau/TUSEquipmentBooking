package com.example.tusequipmentbooking.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
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
    suspend fun insertAllEquipment(equipment: List<Equipment>)

    @Insert
    suspend fun insertEquipment(equipment: Equipment): Long
}
