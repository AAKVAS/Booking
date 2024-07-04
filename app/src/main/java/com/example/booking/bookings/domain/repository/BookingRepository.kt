package com.example.booking.bookings.domain.repository

import com.example.booking.bookings.domain.model.Booking
import com.example.booking.bookings.domain.model.BookingEvent
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

    /**
     * Сохранить запись о бронировании, чтобы в дальнейшем отправить уведомление
     */
    suspend fun insertBookingEvent(booking: Booking)

    /**
     * Получить список предстоящих бронирований
     */
    suspend fun getComingBookingEvents(): List<BookingEvent>

    /**
     * Удалить устаревшие данные о бронированиях
     */
    suspend fun deleteOldEvents()
}