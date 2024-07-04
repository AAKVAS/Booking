package com.example.booking.common.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

/**
 * Переводит дату из одно формата в другой
 */
fun convertDate(
    dateString: String,
    originFormat: String = "dd-MM-yyyy",
    resultFormat: String = "dd MMMM yyyy",
): String {
    val dateFormat = SimpleDateFormat(originFormat, Locale.getDefault())

    val today = Calendar.getInstance().time
    val yesterday = Calendar.getInstance().apply { this.add(Calendar.DATE, -1) }.time
    val tomorrow = Calendar.getInstance().apply { this.add(Calendar.DATE, 1) }.time

    val todayDate = dateFormat.format(today)
    val yesterdayDate = dateFormat.format(yesterday.time)
    val tomorrowDate = dateFormat.format(tomorrow.time)

    return when (dateString) {
        todayDate -> "Сегодня"
        yesterdayDate -> "Вчера"
        tomorrowDate -> "Завтра"
        else -> {
            val date = dateFormat.parse(dateString) ?: return ""
            val newFormat = SimpleDateFormat(resultFormat,  Locale.getDefault())
            newFormat.format(date)
        }
    }
}

/**
 * Текущая дата в виде количества миллисекунд
 */
fun currentDay(): Long {
    val millis = System.currentTimeMillis()
    return millis - millis % 86400000
}

/**
 * Текущее количество часов в минутах
 */
fun currentHoursInMinutes(): Int {
    val millis = System.currentTimeMillis()
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    return hour * 60
}
