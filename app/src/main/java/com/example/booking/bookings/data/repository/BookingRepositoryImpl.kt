package com.example.booking.bookings.data.repository

import com.example.booking.bookings.data.datasource.BookingAPI
import com.example.booking.bookings.domain.model.Booking
import com.example.booking.bookings.domain.repository.BookingRepository
import javax.inject.Inject

/**
 * Реализация репозитория для бронирования мест
 */
class BookingRepositoryImpl @Inject constructor(
    private val api: BookingAPI
) : BookingRepository {
    override suspend fun bookPlace(userLogin: String, booking: Booking) {
        api.bookPlace(userLogin, booking)
    }
}