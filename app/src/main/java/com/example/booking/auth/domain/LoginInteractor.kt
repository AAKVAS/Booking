package com.example.booking.auth.domain

import com.example.booking.auth.domain.model.LoginDetails
import com.example.booking.auth.domain.model.LoginResult
import com.example.booking.auth.domain.model.RegisterResult
import com.example.booking.auth.domain.model.UserDetails
import com.example.booking.auth.domain.repository.LoginRepository
import com.example.booking.common.domain.NetworkInteractor
import com.example.booking.common.domain.repository.RemoteRepository
import javax.inject.Inject

/**
 * Interactor для выполнения авторизации в приложении
 */
class LoginInteractor @Inject constructor(
    private val loginRepository: LoginRepository,
    remoteRepository: RemoteRepository
) : NetworkInteractor(remoteRepository) {
    /**
     * Попытка войти в приложении. Истина, если вход произошёл
     */
    suspend fun login(loginDetails: LoginDetails): Boolean {
        return try {
            loginRepository.login(loginDetails) is LoginResult.Success
        } catch (ex: Exception) {
            false
        }
    }

    /**
     * Попытка зарегестировать профиль
     */
    suspend fun register(registrationDetails: UserDetails): RegisterResult {
        return try {
            loginRepository.register(registrationDetails)
        } catch (ex: Exception) {
            RegisterResult.Failure(UNEXPECTED_EXCEPTION)
        }
    }

    companion object {
        const val UNEXPECTED_EXCEPTION = "Непредвиденная ошибка при попытке регистрации"
    }
}