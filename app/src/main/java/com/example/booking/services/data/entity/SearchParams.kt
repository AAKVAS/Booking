package com.example.booking.services.data.entity

data class SearchParams(
    val userLogin: String,
    val searchPattern: String,
    val cityId: Long
)
