package com.example.booking.auth.domain.model

/**
 * Результат попытки зарегестрировать аккаунт
 */
sealed interface RegisterResult {
    /**
     * Успешно зарегестрирован аккаунт. Содержит данные о зарегестрированном пользователе
     */
    data class Success(val userDetails: UserDetails): RegisterResult

    /**
     * Не удалось создать аккаунт. Содержит текст ошибки.
     */
    data class Failure(val errorMessage: String): RegisterResult
}