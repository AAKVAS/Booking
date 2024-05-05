package com.example.booking.bookings.domain

import com.example.booking.auth.domain.repository.LoginRepository
import com.example.booking.bookings.domain.model.Booking
import com.example.booking.bookings.domain.repository.BookingRepository
import com.example.booking.establishments.domain.model.Place
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Интерактор экрана бронирования
 */
class BookingInteractor  @Inject constructor(
    private val loginRepository: LoginRepository,
    private val bookingRepository: BookingRepository
) {
    /**
     * Забронировать место
     */
    suspend fun bookPlace(booking: Booking): Result<Unit> {
        return runCatching {
            bookingRepository.bookPlace(getUserLogin(), booking)
        }
    }

    suspend fun isUserLogged(): Boolean {
        return loginRepository.isUserLogged()
    }

    private suspend fun getUserLogin(): String {
        return if (loginRepository.isUserLogged()) {
            loginRepository.getUserDetails().first().login
        } else {
            ""
        }
    }

    suspend fun getPlaces(hallId: Long, startedAt: Int, endedAt: Int, date: Long): List<Place> {
        return bookingRepository.getPlaces(hallId, startedAt, endedAt, date)
    }
}