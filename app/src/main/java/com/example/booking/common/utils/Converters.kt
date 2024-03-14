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

fun timeToInt(hours: Int, minutes: Int): Int = hours * 60 + minutes

inline fun Int.toTime(block: (hours: Int, minutes: Int) -> Unit) {
    require(this in 0..1499)
    block(this / 60, this % 60)
}

fun Int.toTextTime(): String {
    require(this in 0..1499)
    val hours = this / 60
    val minutes = this % 60
    return "${String.format("%02d", hours)}:${String.format("%02d", minutes)}"
}

fun currentDay(): Long {
    val millis = System.currentTimeMillis()
    return millis - millis % 86400000
}

@SuppressLint("SimpleDateFormat")
fun Long.millisToDate(format: String = "dd.MM.yyyy"): String {
    require(this >= 0)
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return SimpleDateFormat(format).format(calendar.time)
}

fun currentHoursInMinutes(): Int {
    val millis = System.currentTimeMillis()
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    return hour * 60
}