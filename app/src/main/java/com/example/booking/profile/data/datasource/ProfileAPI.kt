package com.example.booking.profile.data.datasource

import com.example.booking.profile.data.network.BookingHistoryJson
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * API для работы со сведениями о профиле пользователя
 */
interface ProfileAPI {
    /**
     * Возвращает историю бронирований
     */
    @GET("/profile/history")
    suspend fun getBookingHistory(
        @Query("userLogin") userLogin: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): List<BookingHistoryJson>

    /**
     * Отменяет бронирование
     */
    @POST("/profile/history/cancel-booking")
    @FormUrlEncoded
    suspend fun cancelBooking(
        @Field("userLogin") userLogin: String,
        @Field("bookingId") bookingId: Long
    )

    /**
     * Удаляет бронирование из истории
     */
    @POST("/profile/history/delete-booking")
    @FormUrlEncoded
    suspend fun deleteBooking(
        @Field("userLogin") userLogin: String,
        @Field("bookingId") bookingId: Long
    )
}