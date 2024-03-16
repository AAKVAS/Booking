package com.example.booking.bookings.domain.repository

import com.example.booking.bookings.domain.model.Booking

/**
 * Репозиторий для бронирования мест
 */
interface BookingRepository {
    /**
     * Забронировать место
     */
    suspend fun bookPlace(userLogin: String, booking: Booking)
}