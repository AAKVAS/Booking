package com.example.booking.bookings.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

import com.example.booking.bookings.data.entity.DatabaseBookingEvent

@Dao
interface BookingEventDao {
    @Insert
    suspend fun insertBookingEvent(event: DatabaseBookingEvent)

    @Query("SELECT * FROM booking_event")
    suspend fun getBookingEvents(): List<DatabaseBookingEvent>

    @Query("DELETE FROM booking_event WHERE date < :date")
    suspend fun deleteOld(date: Long)

    @Query("DELETE FROM booking_event WHERE uid = :uid")
    suspend fun deleteByUid(uid: String)
}
