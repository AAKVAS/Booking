package com.example.booking.auth.data.datasource

import com.example.booking.auth.data.network.ChangePasswordBody
import com.example.booking.auth.domain.model.UserDetails
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Источник данных для аутентификации с backend'а
 */
interface CloudLoginAPI {
    /**
     * Попытка войти в аккаунт, возвращает [UserDetails] если удалось войти в аккаунт.
     */
    @GET("login")
    suspend fun login(
        @Query("login") login: String,
        @Query("password") password: String,
    ): UserDetails

    /**
     * Попытка зарегистрировать аккаунт, возвращает [UserDetails] если удалось создать аккаунт.
     */
    @POST("registration")
    suspend fun register(@Body registrationDetails: UserDetails): UserDetails

    /**
     * Сохранить данные о пользователе
     */
    @POST("profile")
    suspend fun saveUserDetails(@Body userDetails: UserDetails): UserDetails

    /**
     * Изменить пароль
     */
    @POST("profile/change-password")
    suspend fun changePassword(@Body changePasswordBody: ChangePasswordBody): Any
}