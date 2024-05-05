package com.example.booking.auth.data.repository

import com.example.booking.auth.data.datasource.CloudLoginAPI
import com.example.booking.auth.data.datasource.LocalLoginDatasource
import com.example.booking.auth.data.network.ChangePasswordBody
import com.example.booking.auth.domain.model.LoginDetails
import com.example.booking.auth.domain.model.LoginResult
import com.example.booking.auth.domain.model.RegisterResult
import com.example.booking.auth.domain.model.UserDetails
import com.example.booking.auth.domain.repository.LoginRepository
import com.example.booking.profile.domain.model.ChangePasswordResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Реализация репозитория для аутентификации
 */
class LoginRepositoryImpl (
    private val localDatasource: LocalLoginDatasource,
    private val cloudDatasource: CloudLoginAPI
) : LoginRepository {

    override suspend fun login(loginDetails: LoginDetails): LoginResult {
        return try {
            val loginResult = cloudDatasource.login(loginDetails.login, loginDetails.password)
            localDatasource.saveUserDetails(loginResult)
            LoginResult.Success(loginResult)
        } catch (ignored: Exception) {
            LoginResult.Failure(WRONG_LOGIN_DATA)
        }
    }

    override suspend fun register(registrationDetails: UserDetails): RegisterResult {
        return try {
            val registrationResult = cloudDatasource.register(registrationDetails)
            localDatasource.saveUserDetails(registrationResult)
            RegisterResult.Success(registrationResult)
        } catch (ignored: Exception) {
            RegisterResult.Failure(REGISTRATION_FAILED)
        }
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
        return try {
            val changePasswordBody = ChangePasswordBody(
                login = loggedUser.login,
                oldPassword = oldPassword,
                newPassword = newPassword
            )
            cloudDatasource.changePassword(changePasswordBody)
            return ChangePasswordResult.Success
        } catch (ignored: Exception) {
            return ChangePasswordResult.Failure(CHANGE_PASSWORD_FAILURE)
        }

    }

    companion object {
        private const val NOT_LOGGED_CHANGE_PASSWORD = "Нужно войти в аккаунт прежде, чем пытаться поменять пароль"
        private const val WRONG_LOGIN_DATA = "Неверные данные для входа"
        private const val REGISTRATION_FAILED = "Не удалось зарегестрировать аккаунт"
        private const val CHANGE_PASSWORD_FAILURE = "Не удалось изменить пароль"
    }
}