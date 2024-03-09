package com.example.booking.profile.domain

import com.example.booking.auth.domain.model.UserDetails
import com.example.booking.auth.domain.repository.LoginRepository
import com.example.booking.profile.domain.model.ChangePasswordResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Интерактор для работы с настройками аккаунта пользователя приложения
 */
class SettingsInteractor @Inject constructor(
    private val loginRepository: LoginRepository
) {
    /**
     * Данные о пользователе приложения
     */
    fun getUserDetails(): Flow<UserDetails> {
        return loginRepository.getUserDetails()
    }

    /**
     * Сохранить данные о пользователе
     */
    suspend fun saveUserDetails(userDetails: UserDetails): Result<Unit> {
        return runCatching {
            loginRepository.saveUserDetails(userDetails)
        }
    }

    /**
     * Выйти из аккаунта
     */
    suspend fun logout(): Result<Unit> {
        return runCatching {
            loginRepository.logout()
        }
    }

    /**
     * Изменить пароль
     */
    suspend fun changePassword(oldPassword: String, newPassword: String): ChangePasswordResult {
        return loginRepository.changePassword(oldPassword, newPassword)
    }
}