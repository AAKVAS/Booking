package com.example.booking.establishments.data.network

import com.example.booking.common.utils.getUUID
import com.example.booking.establishments.domain.model.City
import com.example.booking.establishments.domain.model.Hall
import com.example.booking.establishments.domain.model.Place
import com.example.booking.establishments.domain.model.PlaceCoordinate
import com.example.booking.establishments.domain.model.Establishment

fun CityJson.toModel(): City =
    City(
        uid = getUUID(),
        id = id,
        name = name
    )

fun EstablishmentJson.toModel(): Establishment =
    Establishment(
        uid = getUUID(),
        id = id,
        title = title,
        description = description,
        address = address,
        favorite = favorite,
        halls = halls.map { it.toModel() },
        imageLink = imageLink
    )

fun HallJson.toModel(): Hall =
    Hall(
        uid = getUUID(),
        id = id,
        title = title,
        places = places.map { it.toModel() }
    )

fun PlaceJson.toModel(): Place =
    Place(
        uid = getUUID(),
        id = id,
        coordinates = coordinates.map { it.toModel() },
        isFree = isFree,
        peopleAmount = peopleAmount
    )

fun PlaceCoordinateJson.toModel(): PlaceCoordinate =
    PlaceCoordinate(
        uid = getUUID(),
        id = id,
        xPosition = xPosition,
        yPosition = yPosition
    )
