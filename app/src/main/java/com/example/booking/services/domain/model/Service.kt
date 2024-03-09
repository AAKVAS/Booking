package com.example.booking.services.domain.model

import kotlinx.serialization.Serializable

/**
 * Услуга
 */
@Serializable
data class Service(
    val uid: String,
    val id: Long,
    val title: String,
    val description: String,
    val address: String,
    val favorite: Boolean,
    val halls: List<Hall>,
    val imageLink: String
)