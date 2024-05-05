package com.example.booking.bookings.ui.model

sealed interface DateTimeItem {
    data class Date(
        val title: String,
        val date: Long,
        val onClick: () -> Unit = {}
    ) : DateTimeItem

    data class Time(
        val title: String,
        val time: Int,
        val onClick: () -> Unit = {}
    )
}