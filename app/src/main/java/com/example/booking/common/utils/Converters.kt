package com.example.booking.common.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar

@SuppressLint("SimpleDateFormat")
fun Long.toStringDate(format: String = "dd.MM.yyyy"): String {
    require(this >= 0)
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return SimpleDateFormat(format).format(calendar.time)
}