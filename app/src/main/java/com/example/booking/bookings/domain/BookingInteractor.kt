package com.example.booking.bookings.domain

import com.example.booking.auth.domain.repository.LoginRepository
import com.example.booking.bookings.domain.model.Booking
import com.example.booking.bookings.domain.repository.BookingRepository
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

    private suspend fun getUserLogin(): String {
        return if (loginRepository.isUserLogged()) {
            loginRepository.getUserDetails().first().login
        } else {
            ""
        }
    }
}