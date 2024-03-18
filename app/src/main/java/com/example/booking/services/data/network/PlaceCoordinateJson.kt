package com.example.booking.services.data.network

import kotlinx.serialization.Serializable

/**
 * Координаты места в зале
 */
@Serializable
data class PlaceCoordinateJson(
    val id: Long,
    val xPosition: Int,
    val yPosition: Int
) : java.io.Serializable