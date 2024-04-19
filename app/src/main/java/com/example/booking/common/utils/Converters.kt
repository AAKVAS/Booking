package com.example.booking.common.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * Конвертирование даты из Long в строковое значение
 */
@SuppressLint("SimpleDateFormat")
fun Long.toStringDate(format: String = "dd.MM.yyyy"): String {
    require(this >= 0)
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return SimpleDateFormat(format).format(calendar.time)
}

/**
 * Время в часах и минутах в минуты
 */
fun timeToInt(hours: Int, minutes: Int): Int = hours * 60 + minutes

/**
 * Число в минутах в часы и минуты
 */
inline fun Int.toTime(block: (hours: Int, minutes: Int) -> Unit) {
    require(this in 0..1499)
    block(this / 60, this % 60)
}

/**
 * Время в минутах к строковому отображению времени
 */
fun Int.toTextTime(): String {
    require(this in 0..1499)
    val hours = this / 60
    val minutes = this % 60
    return "${String.format("%02d", hours)}:${String.format("%02d", minutes)}"
}

/**
 * Миллисекунды к дате
 */
@SuppressLint("SimpleDateFormat")
fun Long.millisToDate(format: String = "dd.MM.yyyy"): String {
    require(this >= 0)
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return SimpleDateFormat(format).format(calendar.time)
}
