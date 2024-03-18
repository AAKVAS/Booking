package com.example.booking.services.data.network

import kotlinx.serialization.Serializable

/**
 * Место в зале, может находиться на нескольких координатах (клеточках)
 */
@Serializable
data class PlaceJson(
    val id: Long,
    val coordinates: List<PlaceCoordinateJson>,
    val isFree: Boolean,
    val peopleAmount: Int
) : java.io.Serializable