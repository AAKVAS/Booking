package com.example.booking.profile.domain.repository

import androidx.paging.PagingData
import com.example.booking.profile.domain.model.BookingHistory
import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий профиля клиента приложения
 */
interface ProfileRepository {
    /**
     * Возвращает поток истории бронирований
     */
    fun getBookingHistory(userLogin: String): Flow<PagingData<BookingHistory>>

    /**
     * Отменить предстоящее бронирование
     */
    suspend fun cancelBooking(userLogin: String, bookingId: Long)
    /**
     * Удалить из истории бронирование
     */
    suspend fun deleteBooking(userLogin: String, bookingId: Long)
}