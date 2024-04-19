package com.example.booking.bookings.domain.model

import com.example.booking.establishments.domain.model.Hall
import com.example.booking.establishments.domain.model.Place

/**
 * Сведения о бронировании места в заведении
 */
data class Booking(
    val uid: String,
    val hall: Hall,
    val place: Place?,
    val startedAt: Int,
    val endedAt: Int,
    val date: Long
)