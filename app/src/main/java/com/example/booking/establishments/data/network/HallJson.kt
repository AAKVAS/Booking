package com.example.booking.establishments.data.network

import kotlinx.serialization.Serializable

/**
 * Зал, в котором находятся места заведения
 */
@Serializable
data class HallJson(
    val uid: String,
    val id: Long,
    val title: String,
    val places: List<PlaceJson>
) : java.io.Serializable