package com.example.booking.services.domain

import com.example.booking.auth.domain.repository.LoginRepository
import javax.inject.Inject

class CatalogInteractor @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend fun isUserLogged(): Boolean {
        return loginRepository.isUserLogged()
    }
}