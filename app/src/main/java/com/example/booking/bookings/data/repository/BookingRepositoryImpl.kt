package com.example.booking.bookings.data.repository

import com.example.booking.bookings.data.datasource.BookingAPI
import com.example.booking.bookings.data.datasource.BookingEventDao
import com.example.booking.bookings.data.network.createBookPlaceRequest
import com.example.booking.bookings.domain.model.Booking
import com.example.booking.bookings.domain.model.BookingEvent
import com.example.booking.bookings.domain.repository.BookingRepository
import com.example.booking.common.utils.currentDay
import com.example.booking.common.utils.currentHoursInMinutes
import com.example.booking.common.utils.toStringDate
import com.example.booking.establishments.data.network.toModel
import com.example.booking.establishments.domain.model.Place
import javax.inject.Inject

/**
 * Реализация репозитория для бронирования мест
 */
class BookingRepositoryImpl @Inject constructor(
    private val api: BookingAPI,
    private val bookingEventDao: BookingEventDao
) : BookingRepository {
    override suspend fun bookPlace(userLogin: String, booking: Booking) {
        api.bookPlace(createBookPlaceRequest(userLogin, booking))
    }

    override suspend fun getPlaces(hallId: Long, startedAt: Int, endedAt: Int, date: Long): List<Place> {
        return api.getPlaces(
            hallId = hallId,
            startedAt = startedAt,
            endedAt = endedAt,
            date = date.toStringDate(format = "yyyy-dd-MM")
        ).map { it.toModel() }
    }

    override suspend fun insertBookingEvent(booking: Booking) {
        bookingEventDao.insertBookingEvent(booking.toBookingEventEntity())
    }

    override suspend fun getComingBookingEvents(): List<BookingEvent> {
        return bookingEventDao.getBookingEvents().filter {
            val date = it.date - it.date % 86400000
            date == currentDay() && it.startedAt - 90 <= currentHoursInMinutes()
        }.map {
            it.toModel()
        }
    }

    override suspend fun deleteOldEvents() {
        bookingEventDao.deleteOld(currentDay())
    }
}