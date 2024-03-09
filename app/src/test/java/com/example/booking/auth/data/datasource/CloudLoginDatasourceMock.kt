package com.example.booking.auth.data.datasource

import com.example.booking.auth.data.repository.LoginRepositoryImplTest
import com.example.booking.auth.domain.model.LoginResult
import com.example.booking.auth.domain.model.RegisterResult
import com.example.booking.auth.domain.model.RegistrationDetails
import com.example.booking.auth.domain.model.UserDetails
import com.example.booking.profile.domain.model.ChangePasswordResult

/**
 * Заглушка [CloudLoginDatasource]
 */
class CloudLoginDatasourceMock : CloudLoginDatasource {
    private val users: MutableList<User> = mutableListOf(
        User(
            login = LoginRepositoryImplTest.EXISTING_USER_LOGIN,
            password = LoginRepositoryImplTest.EXISTING_USER_PASSWORD,
            details = LoginRepositoryImplTest.EXISTING_USER_DETAILS
        )
    )

    override suspend fun login(login: String, password: String): LoginResult {
        val user: User? = users.firstOrNull { user ->
            user.login == login && user.password == password
        }
        return if (user == null) {
            LoginResult.Failure(WRONG_LOGIN_DETAILS)
        } else {
            LoginResult.Success(user.details)
        }
    }

    override suspend fun register(registrationDetails: RegistrationDetails): RegisterResult {
        val userExists: Boolean = users.any { user ->
            user.login == registrationDetails.login
        }
        return if (userExists) {
            RegisterResult.Failure(USER_EXISTS)
        } else {
            val userDetails = UserDetails(
                token = "test",
                login = registrationDetails.login,
                lastname = registrationDetails.lastname,
                firstname = registrationDetails.firstname,
                birthday = registrationDetails.birthday,
            )
            users.add(
                User(
                    login = registrationDetails.login,
                    password = registrationDetails.password,
                    details = userDetails
                )
            )
            RegisterResult.Success(userDetails)
        }
    }

    override suspend fun saveUserDetails(userDetails: UserDetails): Result<Unit> {
        return runCatching {
            val index = users.indexOfFirst { user ->
                user.login == userDetails.login
            }
            users[index] = users[index].copy(details = userDetails)
        }
    }

    override suspend fun changePassword(
        login: String,
        oldPassword: String,
        newPassword: String
    ): ChangePasswordResult {
        val index = users.indexOfFirst { user ->
            user.login == login
        }
        return if (index == -1) {
            ChangePasswordResult.Failure(USER_NOT_EXISTS)
        } else if (users[index].password != oldPassword) {
            ChangePasswordResult.WrongOldPassword
        } else {
            users[index] = users[index].copy(password = newPassword)
            ChangePasswordResult.Success
        }
    }

    companion object {
        const val WRONG_LOGIN_DETAILS = "Неверные данные для входа"
        const val USER_EXISTS = "Пользователь уже сущестует"
        const val USER_NOT_EXISTS = "Пользователь не найден"
    }
}

data class User(
    val login: String,
    val password: String,
    val details: UserDetails
)