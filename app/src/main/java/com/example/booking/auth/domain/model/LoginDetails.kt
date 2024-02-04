package com.example.booking.auth.domain.model

/**
 * Данные для выполнения входа в аккаунт
 */
data class LoginDetails(
    val login: String,
    val password: String
)
