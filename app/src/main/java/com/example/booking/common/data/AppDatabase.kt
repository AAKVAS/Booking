package com.example.booking.common.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.booking.common.data.entity.BaseEntity
import com.example.booking.common.data.AppDatabase.Companion.VERSION


@Database(
    entities = [
        BaseEntity::class
    ],
    version = VERSION
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val VERSION = 1
        const val DATABASE_NAME = "bookings"
    }
}