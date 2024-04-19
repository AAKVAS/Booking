package com.example.booking.profile.ui.model

import com.example.booking.establishments.domain.model.Establishment

/**
 * Элемент списка истории бронирований
 */
sealed interface BookingHistoryUiItem {
    /**
     * Дата на которую создано бронирование
     */
    data class Date(
        val uid: String,
        val date: String
    ): BookingHistoryUiItem

    /**
     * Сведение о бронировании
     */
    data class Booking (
        val uid: String,
        val id: Long,
        val establishment: Establishment,
        val statusId: Int,
        val startedAt: Int,
        val endedAt: Int,
    ) : BookingHistoryUiItem
}