package com.example.booking.auth.data.datasource

import com.example.booking.auth.domain.model.LoginResult
import com.example.booking.auth.domain.model.RegisterResult
import com.example.booking.auth.domain.model.UserDetails
import com.example.booking.auth.domain.model.RegistrationDetails
import com.example.booking.profile.domain.model.ChangePasswordResult

/**
 * Источник данных для аутентификации с backend'а
 */
interface CloudLoginDatasource {
    /**
     * Попытка войти в аккаунт, возвращает [LoginResult.Success] если произошёл успешный вход.
     * Иначе [LoginResult.Failure]
     */
    suspend fun login(login: String, password: String): LoginResult
    /**
     * Попытка зарегистрировать аккаунт, возвращает [RegisterResult.Success] если удалось создать аккаунт.
     * Иначе [RegisterResult.Failure]
     */
    suspend fun register(registrationDetails: RegistrationDetails): RegisterResult

    /**
     * Сохранить данные о пользователе
     */
    suspend fun saveUserDetails(userDetails: UserDetails): Result<Unit>

    /**
     * Изменить пароль
     */
    suspend fun changePassword(login: String, oldPassword: String, newPassword: String): ChangePasswordResult
}