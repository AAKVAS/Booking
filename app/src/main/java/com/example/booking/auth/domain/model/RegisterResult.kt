package com.example.booking.auth.domain.model

/**
 * Результат попытки зарегистрировать аккаунт
 */
sealed interface RegisterResult {
    /**
     * Успешно зарегистрирован аккаунт. Содержит данные о зарегистрированном пользователе
     */
    data class Success(val userDetails: UserDetails): RegisterResult

    /**
     * Не удалось создать аккаунт. Содержит текст ошибки.
     */
    data class Failure(val errorMessage: String): RegisterResult
}