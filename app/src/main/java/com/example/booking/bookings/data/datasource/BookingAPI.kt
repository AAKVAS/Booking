package com.example.booking.bookings.data.datasource

import com.example.booking.bookings.domain.model.Booking

interface BookingAPI {
    suspend fun bookPlace(userLogin: String, booking: Booking)
}