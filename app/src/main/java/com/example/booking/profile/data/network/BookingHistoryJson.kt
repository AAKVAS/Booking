package com.example.booking.profile.data.network

import kotlinx.serialization.Serializable

/**
 * Список бронирований за определённый день
 */
@Serializable
data class BookingHistoryJson (
    val date: String,
    val items: List<BookingHistoryItemJson>
)