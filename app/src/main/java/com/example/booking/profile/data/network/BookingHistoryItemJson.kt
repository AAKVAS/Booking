package com.example.booking.profile.data.network

import kotlinx.serialization.Serializable

/**
 * Бронирование, отображаемое в истории клиента
 */
@Serializable
data class BookingHistoryItemJson (
    val id: Long,
    val service: BookingServiceJson,
    val statusId: Int,
    val status: String,
    val startedAt: Int,
    val endedAt: Int,
)