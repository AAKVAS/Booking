package com.example.booking.services.data.network

import com.example.booking.services.domain.model.Hall
import kotlinx.serialization.Serializable

/**
 * Филиал заведения
 */
@Serializable
data class ServiceJson(
    val id: Long,
    val title: String,
    val description: String,
    val address: String,
    val favorite: Boolean,
    val halls: List<HallJson>,
    val imageLink: String
)