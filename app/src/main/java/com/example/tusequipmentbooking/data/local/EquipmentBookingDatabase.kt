package com.example.tusequipmentbooking.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tusequipmentbooking.model.Booking
import com.example.tusequipmentbooking.model.Category
import com.example.tusequipmentbooking.model.Equipment

@Database(
    entities = [Category::class, Equipment::class, Booking::class],
    version = 2,
    exportSchema = true
)

abstract class EquipmentBookingDatabase : RoomDatabase() {
    abstract fun equipmentBookingDao(): EquipmentBookingDao
    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
            CREATE TABLE IF NOT EXISTS `bookings` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `equipmentId` INTEGER NOT NULL,
                `userUid` TEXT NOT NULL,
                `durationDays` INTEGER NOT NULL,
                `purpose` TEXT NOT NULL,
                `status` TEXT NOT NULL,
                FOREIGN KEY(`equipmentId`) REFERENCES `equipment`(`id`)
                    ON UPDATE NO ACTION ON DELETE RESTRICT
            )
        """.trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_bookings_equipmentId` ON `bookings` (`equipmentId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_bookings_userUid` ON `bookings` (`userUid`)")
            }
        }
        @Volatile private var instance: EquipmentBookingDatabase? = null
        fun getInstance(context: Context): EquipmentBookingDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    EquipmentBookingDatabase::class.java,
                    "equipmentbookingdatabase.db"
                ).addMigrations(MIGRATION_1_2)
                    .build().also { instance = it }
            }
    }
}
