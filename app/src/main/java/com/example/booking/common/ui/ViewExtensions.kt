package com.example.booking.common.ui

import android.app.AlertDialog
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.booking.R
import com.google.android.material.datepicker.MaterialDatePicker

/**
 * Показать диалоговое окно для выбора даты
 */
fun Fragment.showDatePicker(
    tag: String,
    date: Long = MaterialDatePicker.todayInUtcMilliseconds(),
    onPosClick: (selection: Long) -> Unit = {}
) {
    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText(getString(R.string.choose_date))
        .setSelection(date)
        .build()

    datePicker.addOnPositiveButtonClickListener {
        onPosClick(datePicker.selection ?: MaterialDatePicker.todayInUtcMilliseconds())
    }

    datePicker.show(childFragmentManager, tag)
}
