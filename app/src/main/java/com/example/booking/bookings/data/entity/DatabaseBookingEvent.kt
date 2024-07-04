package com.example.booking.bookings.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность базы данных Room о предстоящем бронировании
 */
@Entity(
    tableName = "booking_event"
)
data class DatabaseBookingEvent(
    @PrimaryKey(autoGenerate = false)
    val uid: String,
    val date: Long,
    @ColumnInfo(name = "started_at")
    val startedAt: Int,
    @ColumnInfo(name = "ended_at")
    val endedAt: Int
)
