package com.example.booking.profile.data.network

import kotlinx.serialization.Serializable

/**
 * Сведения о заведении, в октором было забронировано место
 */
@Serializable
data class BookingEstablishmentJson(
    val id: Long,
    val title: String,
    val address: String,
    val imageLink: String
)