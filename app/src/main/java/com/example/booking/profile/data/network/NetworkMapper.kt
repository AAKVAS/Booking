package com.example.booking.profile.data.network

import com.example.booking.common.utils.getUUID
import com.example.booking.profile.domain.model.BookingHistory
import com.example.booking.profile.domain.model.BookingHistoryItem
import com.example.booking.establishments.domain.model.Establishment

fun BookingHistoryJson.toModel(): BookingHistory =
    BookingHistory(
        uid = getUUID(),
        date = date,
        items = items.map { it.toModel() }
    )

fun BookingHistoryItemJson.toModel(): BookingHistoryItem =
    BookingHistoryItem(
        uid = getUUID(),
        id = id,
        establishment = establishment.toModel(),
        statusId = statusId,
        startedAt = startedAt,
        endedAt = endedAt,
    )

fun BookingEstablishmentJson.toModel(): Establishment =
    Establishment(
        uid = getUUID(),
        id = id,
        title = title,
        description = "",
        address = address,
        favorite = false,
        halls = listOf(),
        imageLink = imageLink
    )