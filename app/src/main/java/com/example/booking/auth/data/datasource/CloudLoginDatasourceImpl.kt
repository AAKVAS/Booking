package com.example.booking.auth.data.datasource

import com.example.booking.auth.data.entity.LoginResult
import com.example.booking.auth.data.entity.RegisterResult
import com.example.booking.auth.data.entity.UserDetails
import com.example.booking.auth.domain.model.RegistrationDetails

/**
 * Реализация источника данных для аутентификации с backend'a
 */
class CloudLoginDatasourceImpl : CloudLoginDatasource {

    //TODO: удалить после создания бэка
    private val fakeUserDetails: UserDetails =
        UserDetails(
            token = "test",
            login = "plotan",
            lastname = "lastname",
            firstname = "firstname",
            birthday = 0,
        )

    override suspend fun login(login: String, password: String): LoginResult {
        return if (login == "plotan") {
            LoginResult.Success(fakeUserDetails)
        } else {
            LoginResult.Failure(WRONG_LOGIN_DETAILS)
        }
    }

    override suspend fun register(registrationDetails: RegistrationDetails): RegisterResult {
        return RegisterResult.Success(fakeUserDetails)
    }

    override suspend fun saveUserDetails(userDetails: UserDetails): Result<Unit> {
        return Result.success(Unit)
    }

    companion object {
        const val WRONG_LOGIN_DETAILS = "Неверные данные для входа"
    }
}