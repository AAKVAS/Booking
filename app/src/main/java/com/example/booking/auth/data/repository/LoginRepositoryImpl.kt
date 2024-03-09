package com.example.booking.auth.data.repository

import com.example.booking.auth.data.datasource.CloudLoginDatasource
import com.example.booking.auth.data.datasource.LocalLoginDatasource
import com.example.booking.auth.domain.model.LoginResult
import com.example.booking.auth.domain.model.RegisterResult
import com.example.booking.auth.domain.model.UserDetails
import com.example.booking.auth.domain.model.LoginDetails
import com.example.booking.auth.domain.model.RegistrationDetails
import com.example.booking.auth.domain.repository.LoginRepository
import com.example.booking.profile.domain.model.ChangePasswordResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Реализация репозитория для аутентификации
 */
class LoginRepositoryImpl @Inject constructor (
    private val localDatasource: LocalLoginDatasource,
    private val cloudDatasource: CloudLoginDatasource
) : LoginRepository {

    override suspend fun login(loginDetails: LoginDetails): LoginResult {
        val loginResult = cloudDatasource.login(loginDetails.login, loginDetails.password)
        if (loginResult is LoginResult.Success) {
            localDatasource.saveUserDetails(loginResult.userDetails)
        }
        return loginResult
    }

    override suspend fun register(registrationDetails: RegistrationDetails): RegisterResult {
        val registrationResult = cloudDatasource.register(registrationDetails)
        if (registrationResult is RegisterResult.Success) {
            localDatasource.saveUserDetails(registrationResult.userDetails)
        }
        return registrationResult
    }

    override suspend fun isUserLogged(): Boolean {
        return localDatasource.isUserLogged()
    }

    override fun getUserDetails(): Flow<UserDetails> {
        return localDatasource.getLoggedUserDetails()
    }

    override suspend fun saveUserDetails(userDetails: UserDetails) {
        cloudDatasource.saveUserDetails(userDetails)
        localDatasource.saveUserDetails(userDetails)
    }

    override suspend fun logout() {
        localDatasource.logout()
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String): ChangePasswordResult {
        if (!localDatasource.isUserLogged()) {
            return ChangePasswordResult.Failure(NOT_LOGGED_CHANGE_PASSWORD)
        }
        val loggedUser: UserDetails = localDatasource.getLoggedUserDetails().first()
        return cloudDatasource.changePassword(loggedUser.login, oldPassword, newPassword)
    }

    companion object {
        private const val NOT_LOGGED_CHANGE_PASSWORD = "Нужно войти в аккаунт прежде, чем пытаться поменять пароль"
    }
}