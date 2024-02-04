package com.example.booking.common.domain

import com.example.booking.auth.domain.repository.LoginRepository
import javax.inject.Inject

class MainInteractor @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend fun isUserLogged(): Boolean {
        return loginRepository.isUserLogged()
    }
}