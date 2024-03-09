package com.example.booking.auth.domain.repository

import com.example.booking.auth.domain.model.LoginResult
import com.example.booking.auth.domain.model.RegisterResult
import com.example.booking.auth.domain.model.UserDetails
import com.example.booking.auth.domain.model.LoginDetails
import com.example.booking.auth.domain.model.RegistrationDetails
import com.example.booking.profile.domain.model.ChangePasswordResult
import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий для выполнения аутентификации
 */
interface LoginRepository {
    /**
     * Вход в аккаунт системы
     */
    suspend fun login(loginDetails: LoginDetails): LoginResult
    /**
     * Регистрация в профиле
     */
    suspend fun register(registrationDetails: RegistrationDetails): RegisterResult
    /**
     * Вошёл ли пользователь в аккаунт
     */
    suspend fun isUserLogged(): Boolean
    /**
     * Данные о пользователе приложения
     */
    fun getUserDetails(): Flow<UserDetails>
    /**
     * Сохранить данные о пользователе
     */
    suspend fun saveUserDetails(userDetails: UserDetails)

    /**
     * Выйти из аккаунта
     */
    suspend fun logout()

    /**
     * Изменить пароль
     */
    suspend fun changePassword(oldPassword: String, newPassword: String): ChangePasswordResult
}