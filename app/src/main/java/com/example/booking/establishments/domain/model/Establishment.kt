package com.example.booking.establishments.domain.model

import kotlinx.serialization.Serializable

/**
 * Филиал заведения
 */
@Serializable
data class Establishment(
    val uid: String,
    val id: Long,
    val title: String,
    val description: String,
    val address: String,
    val favorite: Boolean,
    val halls: List<Hall>,
    val imageLink: String
)