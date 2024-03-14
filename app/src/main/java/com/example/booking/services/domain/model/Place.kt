package com.example.booking.services.domain.model

import kotlinx.serialization.Serializable

/**
 * Место в зале, может находиться на нескольких координатах (клеточках)
 */
@Serializable
data class Place(
    val uid: String,
    val id: Long,
    val coordinates: List<PlaceCoordinate>,
    val isFree: Boolean,
    val peopleAmount: Int
) : java.io.Serializable