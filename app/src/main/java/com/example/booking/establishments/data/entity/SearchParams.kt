package com.example.booking.establishments.data.entity

/**
 * Параметры для поиска филиалов заведений.
 * [userLogin] - логин пользователя, используется при выборке избранных заведений.
 * [searchPattern] - подстрока названия заведения.
 * [cityId] - id города. Если любой город, то передавать -1.
 */
data class SearchParams(
    val userLogin: String,
    val searchPattern: String,
    val cityId: Long
)
