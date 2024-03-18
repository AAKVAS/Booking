package com.example.booking.profile.domain.model


/**
 * Сведения о бронированиях за определённый день
 */
data class BookingHistory(
    val uid: String,
    val date: String,
    val items: List<BookingHistoryItem>
)
