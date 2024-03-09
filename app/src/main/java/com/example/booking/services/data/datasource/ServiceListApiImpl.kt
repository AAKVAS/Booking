package com.example.booking.services.data.datasource

import com.example.booking.services.domain.model.Service
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceListApiImpl : ServiceListAPI {

    @GET("services")
    override suspend fun fetchServices(
        @Query("userLogin") userLogin: String,
        @Query("cityId") cityId: Long,
        @Query("searchPattern") searchPattern: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): List<Service>

    override suspend fun getServiceDetails(userLogin: String, serviceId: Long): Service
}