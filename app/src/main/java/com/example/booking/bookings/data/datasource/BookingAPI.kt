package com.example.booking.bookings.data.datasource

import com.example.booking.bookings.data.network.BookPlaceRequest
import com.example.booking.establishments.data.network.PlaceJson
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * API для выполнения бронирования
 */
interface BookingAPI {
    /**
     * Забронировать место
     */
    @POST("booking")
    suspend fun bookPlace(@Body requestBody: BookPlaceRequest)

    /**
     * Получить список мест на определённое время и дату
     */
    @GET("/booking/places")
    suspend fun getPlaces(
        @Query("hallId") hallId: Long,
        @Query("date") date: String,
        @Query("startedAt") startedAt: Int,
        @Query("endedAt") endedAt: Int,
    ): List<PlaceJson>
}