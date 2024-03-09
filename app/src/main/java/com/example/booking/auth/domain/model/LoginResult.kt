package com.example.booking.auth.domain.model

/**
 * Результат попытки входа в систему
 */
sealed interface LoginResult {
    /**
     * Успех, содержит данные о пользователе.
     */
    data class Success(val userDetails: UserDetails): LoginResult

    /**
     * Ошибка входа.
     */
    data class Failure(val errorMessage: String): LoginResult
}