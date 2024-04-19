package com.example.booking.common.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import androidx.core.content.ContextCompat.getString
import com.example.booking.R

/**
 * Диалоговое окно для ввода строкового значения
 */
class TextInputDialog(
    context: Context,
    title: String,
    onPosClick: (text: String) -> Unit
) {
    private val alertDialog: AlertDialog = AlertDialog.Builder(context).create()
    private val inputEditText = EditText(context)

    init {
        alertDialog.setTitle(title)
        alertDialog.setView(inputEditText)

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(context, R.string.ok)) { _, _ ->
            val enteredText = inputEditText.text.toString()
            onPosClick(enteredText)
        }

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(context, R.string.cancel)) { _, _ ->
            alertDialog.dismiss()
        }
    }

    /**
     * Вызывает окно
     */
    fun show() {
        alertDialog.show()
    }
}