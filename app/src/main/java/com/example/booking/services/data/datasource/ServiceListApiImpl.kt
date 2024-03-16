package com.example.booking.services.data.datasource

import com.example.booking.services.domain.model.City
import com.example.booking.services.domain.model.Service
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceListApiImpl {

    @GET("services")
    suspend fun fetchServices(
        @Query("cityId") cityId: Long,
        @Query("searchPattern") searchPattern: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): List<Service>

    @GET("favorite-services")
    suspend fun fetchFavoriteServices(
        @Query("userLogin") userLogin: String,
        @Query("cityId") cityId: Long,
        @Query("searchPattern") searchPattern: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): List<Service>

    @GET("service")
    suspend fun getServiceDetails(
        @Query("userLogin") userLogin: String,
        @Query("serviceId") serviceId: Long
    ): Service

//    suspend fun setServiceFavorite(userLogin: String, serviceId: Long, favorite: Boolean) {
//
//    }

    @GET("cities")
    suspend fun getCities(): List<City>
}