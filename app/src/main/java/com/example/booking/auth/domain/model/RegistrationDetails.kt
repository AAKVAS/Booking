package com.example.booking.auth.domain.model

/**
 * Данные для выполнения регистрации
 */
data class RegistrationDetails(
    val login: String = "",
    val lastname: String = "",
    val firstname: String = "",
    val birthday: Long = 0,
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = ""
)
