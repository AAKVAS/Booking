package com.example.booking.bookings.domain.repository

import com.example.booking.bookings.domain.model.Booking
import com.example.booking.establishments.domain.model.Place

/**
 * Репозиторий для бронирования мест
 */
interface BookingRepository {
    /**
     * Забронировать место
     */
    suspend fun bookPlace(userLogin: String, booking: Booking)

    /**
     * Получить список мест
     */
    suspend fun getPlaces(hallId: Long, startedAt: Int, endedAt: Int, date: Long): List<Place>
}