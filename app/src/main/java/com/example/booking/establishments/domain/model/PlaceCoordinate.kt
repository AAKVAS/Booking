package com.example.booking.establishments.domain.model

import kotlinx.serialization.Serializable

/**
 * Координаты места в зале
 */
@Serializable
data class PlaceCoordinate(
    val uid: String,
    val id: Long,
    val xPosition: Int,
    val yPosition: Int
) : java.io.Serializable