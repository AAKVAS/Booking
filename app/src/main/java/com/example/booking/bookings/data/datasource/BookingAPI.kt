package com.example.booking.bookings.data.datasource

import com.example.booking.bookings.domain.model.Booking
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * API для выполнения бронирования
 */
interface BookingAPI {
    /**
     * Забронировать место
     */
    @POST("book-place")
    @FormUrlEncoded
    suspend fun bookPlace(
        @Field("userLogin") userLogin: String,
        @Field("booking") booking: Booking
    )
}