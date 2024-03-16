package com.example.booking.common.data

import androidx.room.RoomDatabase


//@Database(
//    entities = [
//
//    ],
//    version = VERSION
//)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val VERSION = 1
        const val DATABASE_NAME = "bookings"
    }
}