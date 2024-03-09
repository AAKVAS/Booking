package com.example.booking.auth.data.datasource

import com.example.booking.auth.domain.model.UserDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocalLoginDatasourceMock : LocalLoginDatasource {
    private var loggedUser: UserDetails? = null

    override suspend fun saveUserDetails(userDetails: UserDetails) {
        loggedUser = userDetails
    }

    override fun getLoggedUserDetails(): Flow<UserDetails> {
        return flow {
            emit(loggedUser!!)
        }
    }

    override suspend fun isUserLogged(): Boolean {
        return loggedUser != null
    }

    override suspend fun logout() {
        loggedUser = null
    }
}