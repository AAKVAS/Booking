package com.example.booking.auth.data.datasource

import com.example.booking.auth.data.entity.UserDetails
import kotlinx.coroutines.flow.Flow

/**
 * Локальный репозиторий для аутентификации
 */
interface LocalLoginDatasource {
    /**
     * Сохранить данные о пользователе.
     */
    suspend fun saveUserDetails(userDetails: UserDetails)
    /**
     * Получить данные о текущем пользователе приложения
     */
    fun getLoggedUserDetails(): Flow<UserDetails>
    /**
     * Вошёл ли пользователь в аккаунт
     */
    suspend fun isUserLogged(): Boolean
}