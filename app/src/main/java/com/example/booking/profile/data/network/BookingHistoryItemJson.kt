package com.example.booking.profile.data.network

import kotlinx.serialization.Serializable

/**
 * Бронирование, отображаемое в истории клиента
 */
@Serializable
data class BookingHistoryItemJson (
    val id: Long,
    val establishment: BookingEstablishmentJson,
    val statusId: Int,
    val startedAt: Int,
    val endedAt: Int,
)