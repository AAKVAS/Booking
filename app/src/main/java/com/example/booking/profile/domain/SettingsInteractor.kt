package com.example.booking.profile.domain

import com.example.booking.auth.data.entity.UserDetails
import com.example.booking.auth.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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
        return try {
            loginRepository.saveUserDetails(userDetails)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}