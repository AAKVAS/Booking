package com.example.booking.auth.data.repository

import com.example.booking.auth.data.datasource.CloudLoginDatasource
import com.example.booking.auth.data.datasource.LocalLoginDatasource
import com.example.booking.auth.data.entity.LoginResult
import com.example.booking.auth.data.entity.RegisterResult
import com.example.booking.auth.data.entity.UserDetails
import com.example.booking.auth.domain.model.LoginDetails
import com.example.booking.auth.domain.model.RegistrationDetails
import com.example.booking.auth.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
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
}