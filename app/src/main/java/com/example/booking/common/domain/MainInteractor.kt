package com.example.booking.common.domain

import com.example.booking.auth.domain.repository.LoginRepository
import javax.inject.Inject

/**
 * Интерактор для главной активности
 */
class MainInteractor @Inject constructor(
    private val loginRepository: LoginRepository
) {
    /**
     * Вошёл ли пользователь в аккаунт
     */
    suspend fun isUserLogged(): Boolean {
        return loginRepository.isUserLogged()
    }
}