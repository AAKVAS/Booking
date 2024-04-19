package com.example.booking.establishments.data.network


import kotlinx.serialization.Serializable

/**
 * Филиал заведения c backend'а
 */
@Serializable
data class EstablishmentJson(
    val id: Long,
    val title: String,
    val description: String,
    val address: String,
    val favorite: Boolean,
    val halls: List<HallJson>,
    val imageLink: String
)