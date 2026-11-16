package com.example.tusequipmentbooking.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tusequipmentbooking.model.Category
import com.example.tusequipmentbooking.model.Equipment

@Database(
    entities = [Category::class, Equipment::class],
    version = 1,
    exportSchema = true
)
abstract class EquipmentBookingDatabase : RoomDatabase() {

    abstract fun equipmentBookingDao(): EquipmentBookingDao

    companion object {
        @Volatile private var instance: EquipmentBookingDatabase? = null

        fun getInstance(context: Context): EquipmentBookingDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    EquipmentBookingDatabase::class.java,
                    "equipmentbooking.db"
                ).build().also { instance = it }
            }
    }
}
