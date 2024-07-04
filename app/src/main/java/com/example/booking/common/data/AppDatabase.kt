package com.example.booking.common.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.booking.bookings.data.datasource.BookingEventDao
import com.example.booking.bookings.data.entity.DatabaseBookingEvent
import com.example.booking.common.data.AppDatabase.Companion.VERSION


/**
 * Room база данных приложения
 */
@Database(
    entities = [
        DatabaseBookingEvent::class
    ],
    version = VERSION
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getBookingEventDao(): BookingEventDao

    companion object {
        const val VERSION = 1
        const val DATABASE_NAME = "bookings"
    }
}