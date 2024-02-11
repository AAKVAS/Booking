package com.example.booking.profile.domain

import com.example.booking.auth.data.entity.UserDetails
import com.example.booking.auth.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Интерактор для работы с профилем пользователя
 */
class ProfileInteractor @Inject constructor(
    private val loginRepository: LoginRepository
) {
    /**
     * Данные о пользователе приложения
     */
    fun getUserDetails(): Flow<UserDetails> {
        return loginRepository.getUserDetails()
    }
}
