package com.example.booking.profile.domain.model

import com.example.booking.services.domain.model.Service

data class BookingHistoryItem(
    val uid: String,
    val id: Long,
    val service: Service,
    val statusId: Int,
    val status: String,
    val startedAt: Int,
    val endedAt: Int,
)
