package com.example.booking.profile.data.network

import com.example.booking.services.domain.model.Hall
import kotlinx.serialization.Serializable

@Serializable
data class BookingServiceJson(
    val id: Long,
    val title: String,
    val address: String,
    val imageLink: String
)