package com.example.tusequipmentbooking.data

import androidx.room.withTransaction
import com.example.tusequipmentbooking.data.local.EquipmentBookingDatabase
import com.example.tusequipmentbooking.model.Equipment

class EquipmentRepository(private val database: EquipmentBookingDatabase) {

    private val dao = database.equipmentBookingDao()

    val equipment = dao.observeEquipment()
    val categories = dao.observeCategories()

    suspend fun seedIfEmpty() {
        database.withTransaction {
            if (dao.categoryCount() == 0 && dao.equipmentCount() == 0) {
                dao.insertCategories(sampleCategories)
                dao.insertAllEquipment(sampleEquipment)
            }
        }
    }

    suspend fun addEquipment(categoryId: Int): Long = dao.insertEquipment(
        Equipment(
            id = 0,
            categoryId = categoryId,
            name = "Test Equipment",
            description = "A new item saved during the Room practical."
        )
    )
}
