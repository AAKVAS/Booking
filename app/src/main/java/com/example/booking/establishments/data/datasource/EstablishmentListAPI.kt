package com.example.booking.establishments.data.datasource

import com.example.booking.establishments.data.network.CityJson
import com.example.booking.establishments.data.network.EstablishmentJson
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * API для работы с филиалами заведений
 */
interface EstablishmentListAPI {

    /**
     * Возвращает список филиалов заведений с фильтрацией по городу или названию
     */
    @GET("establishments")
    suspend fun fetchEstablishments(
        @Query("cityId") cityId: Long,
        @Query("searchPattern") searchPattern: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): List<EstablishmentJson>

    /**
     * Возвращает список избранных филиалов заведений с фильтрацией по городу или названию
     */
    @GET("favorite-establishments")
    suspend fun fetchFavoriteEstablishments(
        @Query("userLogin") userLogin: String,
        @Query("cityId") cityId: Long,
        @Query("searchPattern") searchPattern: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): List<EstablishmentJson>

    /**
     * Возвращает подробную информацию о филиале заведения
     */
    @GET("establishment")
    suspend fun getEstablishmentDetails(
        @Query("userLogin") userLogin: String,
        @Query("establishmentId") establishmentId: Long
    ): EstablishmentJson

    /**
     * Меняет избранность заведения
     */
    @POST("set-favorite-establishment")
    suspend fun setEstablishmentFavorite(@Body requestBody: RequestBody)

    /**
     * Возвращает список городов для фильтрации
     */
    @GET("cities")
    suspend fun getCities(): List<CityJson>
}