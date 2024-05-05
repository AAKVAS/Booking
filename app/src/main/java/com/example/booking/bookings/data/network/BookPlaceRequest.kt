package com.example.booking.bookings.data.network

import java.io.Serializable

data class BookPlaceRequest(
    val userLogin: String,
    val placeId: Long,
    val startedAt: Int,
    val endedAt: Int,
    val date: Long
) : Serializable
