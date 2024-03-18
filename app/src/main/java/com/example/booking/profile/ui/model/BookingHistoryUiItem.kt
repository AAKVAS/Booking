package com.example.booking.profile.ui.model

import com.example.booking.services.domain.model.Service

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
        val service: Service,
        val statusId: Int,
        val status: String,
        val startedAt: Int,
        val endedAt: Int,
    ) : BookingHistoryUiItem
}