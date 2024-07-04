package com.example.booking.bookings.data.repository

import com.example.booking.bookings.data.entity.DatabaseBookingEvent
import com.example.booking.bookings.domain.model.Booking
import com.example.booking.bookings.domain.model.BookingEvent

fun DatabaseBookingEvent.toModel(): BookingEvent {
    return BookingEvent(
        uid = uid,
        date = date,
        startedAt = startedAt,
        endedAt = endedAt
    )
}

fun Booking.toBookingEventEntity(): DatabaseBookingEvent {
    return DatabaseBookingEvent(
        uid = uid,
        date = date,
        startedAt = startedAt,
        endedAt = endedAt
    )
}