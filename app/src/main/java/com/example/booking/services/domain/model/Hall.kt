package com.example.booking.services.domain.model

import kotlinx.serialization.Serializable

/**
 * Зал, в котором находятся места заведения
 */
@Serializable
data class Hall(
    val uid: String,
    val id: Long,
    val title: String,
    val places: List<Place>
) : java.io.Serializable