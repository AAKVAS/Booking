package com.example.booking.bookings.ui.model

import com.example.booking.services.domain.model.Hall
import kotlinx.serialization.Serializable

@Serializable
data class BookingServiceParam(
    val uid: String,
    val id: Long,
    val title: String,
    val description: String,
    val address: String,
    val favorite: Boolean,
    val hall: Hall,
    val imageLink: String
) : java.io.Serializable