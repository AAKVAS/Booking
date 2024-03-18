package com.example.booking.profile.data.datasource

import com.example.booking.profile.data.network.BookingHistoryJson
import com.example.booking.profile.domain.model.BookingHistory
import retrofit2.http.GET
import retrofit2.http.Query

interface ProfileAPI {
    @GET("user-history")
    suspend fun getBookingHistory(
        @Query("userLogin") userLogin: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): List<BookingHistoryJson>
}