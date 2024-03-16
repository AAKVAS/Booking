package com.example.booking.common.data.datasource

import retrofit2.http.GET

interface PingAPI {
    @GET("ping")
    suspend fun ping(): Any
}