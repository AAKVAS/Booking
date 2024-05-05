package com.example.booking.bookings.data.network

import com.example.booking.bookings.domain.model.Booking

fun createBookPlaceRequest(userLogin: String, booking: Booking): BookPlaceRequest {
    return BookPlaceRequest(
        userLogin = userLogin,
        placeId = booking.place!!.id,
        startedAt = booking.startedAt,
        endedAt = booking.endedAt,
        date = booking.date - booking.date % (24 * 60 * 60 * 1000)
    )
}