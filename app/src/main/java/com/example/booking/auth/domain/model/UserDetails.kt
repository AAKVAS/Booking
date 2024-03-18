package com.example.booking.auth.domain.model

/**
 * Данные о текущем пользователе приложения
 */
data class UserDetails(
    val token: String = "",
    val login: String = "",
    val lastname: String = "",
    val firstname: String = "",
    val birthday: Long = 0,
    val needPush: Boolean = true
)