package com.example.booking.services.domain

import com.example.booking.auth.domain.repository.LoginRepository
import javax.inject.Inject

/**
 * Интерактор каталога услуг
 */
class CatalogInteractor @Inject constructor(
    private val loginRepository: LoginRepository
) {
    /**
     * Вошёл ли пользователь в систему
     */
    suspend fun isUserLogged(): Boolean {
        return loginRepository.isUserLogged()
    }
}