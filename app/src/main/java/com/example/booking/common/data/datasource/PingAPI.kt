package com.example.booking.common.data.datasource

import retrofit2.http.GET

/**
 * API для проверки доступности backend'а
 */
interface PingAPI {
    /**
     * Проверить доступность backend'a, если нет ошибки, то сервис доступен
     */
    @GET("ping")
    suspend fun ping(): Any
}