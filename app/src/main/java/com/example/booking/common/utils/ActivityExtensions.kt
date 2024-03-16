package com.example.booking.common.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.booking.R

/**
 * Спрятать клавиатуру
 */
fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

/**
 * Спрятать клавиатуру
 */
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * Показать сообщение, если нет доступа к сети
 */
fun Context.showMessageIfNetworkNotAvailable() {
    if (!isNetworkAvailable()) {
        showNetworkNotAvailableMessage()
    }
}

/**
 * Проверить доступен ли интернет
 */
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

/**
 * Диалоговое окно с сообщением, что нет доступа к сети
 */
fun Context.showNetworkNotAvailableMessage() {
    val builder = AlertDialog.Builder(this)

    builder.setMessage(R.string.network_not_available)
        .setPositiveButton(R.string.ok) { dialog, _ ->
            dialog.dismiss()
        }

    val dialog = builder.create()
    dialog.show()
}
