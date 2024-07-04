package com.example.booking.bookings.domain.model

/**
 * Бронирование, о котором необходимо отправить напоминающее уведомление
 */
data class BookingEvent(
    val uid: String,
    val date: Long,
    val startedAt: Int,
    val endedAt: Int
)
