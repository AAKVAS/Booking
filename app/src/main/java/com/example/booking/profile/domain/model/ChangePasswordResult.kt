package com.example.booking.profile.domain.model

/**
 * Результат попытки изменить пароль
 */
sealed interface ChangePasswordResult {
    /**
     * Пароль успешно изменён
     */
    data object Success: ChangePasswordResult

    /**
     * Неверно введён старый пароль
     */
    data object WrongOldPassword: ChangePasswordResult

    /**
     * Ошибка при попытке сохранить старый пароль
     */
    data class Failure(val errorMessage: String): ChangePasswordResult
}