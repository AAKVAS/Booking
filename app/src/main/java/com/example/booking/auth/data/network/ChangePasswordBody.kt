package com.example.booking.auth.data.network


data class ChangePasswordBody(
    val login: String,
    val oldPassword: String,
    val newPassword: String
)
