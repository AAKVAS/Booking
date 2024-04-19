package com.example.booking.profile.domain

import androidx.paging.PagingData
import com.example.booking.auth.domain.model.UserDetails
import com.example.booking.auth.domain.repository.LoginRepository
import com.example.booking.common.domain.NetworkInteractor
import com.example.booking.common.domain.repository.RemoteRepository
import com.example.booking.profile.domain.model.BookingHistory
import com.example.booking.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Интерактор для работы с профилем пользователя
 */
class ProfileInteractor @Inject constructor(
    private val loginRepository: LoginRepository,
    private val profileRepository: ProfileRepository,
    remoteRepository: RemoteRepository
) : NetworkInteractor(remoteRepository) {
    /**
     * Данные о пользователе приложения
     */
    fun getUserDetails(): Flow<UserDetails> {
        return loginRepository.getUserDetails()
    }

    /**
     * Возвращает поток истории бронирований
     */
    suspend fun getBookingHistory(): Flow<PagingData<BookingHistory>> {
        return profileRepository.getBookingHistory(getUserLogin())
    }

    /**
     * Отменить предстоящее бронирование
     */
    suspend fun cancelBooking(bookingId: Long): Result<Unit> {
        return runCatching {
            profileRepository.cancelBooking(getUserLogin(), bookingId)
        }
    }

    /**
     * Удалить из истории бронирование
     */
    suspend fun deleteBooking(bookingId: Long): Result<Unit> {
        return runCatching {
            profileRepository.deleteBooking(getUserLogin(), bookingId)
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
