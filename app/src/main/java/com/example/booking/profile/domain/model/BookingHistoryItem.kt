package com.example.booking.profile.domain.model

import com.example.booking.establishments.domain.model.Establishment

data class BookingHistoryItem(
    val uid: String,
    val id: Long,
    val establishment: Establishment,
    val statusId: Int,
    val startedAt: Int,
    val endedAt: Int,
)
