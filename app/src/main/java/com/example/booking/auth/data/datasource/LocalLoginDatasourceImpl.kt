package com.example.booking.auth.data.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.booking.auth.domain.model.UserDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


/**
 * Реализация локального репозитория для аутентификации
 */
class LocalLoginDatasourceImpl(private val context: Context)
: LocalLoginDatasource {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)

    override suspend fun saveUserDetails(userDetails: UserDetails) {
        context.dataStore.edit {
            it[USER_LOGIN] = userDetails.login
            it[USER_LASTNAME] = userDetails.lastname
            it[USER_FIRSTNAME] = userDetails.firstname
            it[USER_BIRTHDAY] = userDetails.birthday
            it[NEED_PUSH] = userDetails.needPush
        }
    }

    override fun getLoggedUserDetails(): Flow<UserDetails> {
        return context.dataStore.data
            .filter { it[USER_LOGIN] != null }
            .map {
            UserDetails(
                login = it[USER_LOGIN]!!,
                lastname = it[USER_LASTNAME]!!,
                firstname = it[USER_FIRSTNAME]!!,
                birthday = it[USER_BIRTHDAY]!!,
                needPush = it[NEED_PUSH]!!
            )
        }
    }

    override suspend fun isUserLogged(): Boolean {
        return context.dataStore.data.first()[USER_LOGIN] != null
    }

    override suspend fun logout() {
        context.dataStore.edit {
            it.remove(USER_LOGIN)
            it.remove(USER_LASTNAME)
            it.remove(USER_FIRSTNAME)
            it.remove(USER_BIRTHDAY)
            it.remove(NEED_PUSH)
        }
    }

    companion object {
        private const val PREFERENCE_NAME = "user"

        private val USER_LOGIN = stringPreferencesKey("user_login")
        private val USER_LASTNAME = stringPreferencesKey("user_lastname")
        private val USER_FIRSTNAME = stringPreferencesKey("user_firstname")
        private val USER_BIRTHDAY = longPreferencesKey("user_birthday")
        private val NEED_PUSH = booleanPreferencesKey("need_push")
    }
}