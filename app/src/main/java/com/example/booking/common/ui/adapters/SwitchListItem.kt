package com.example.booking.common.ui.adapters

import android.widget.CompoundButton

data class SwitchListItem(
    val title: String,
    val checked: Boolean,
    val onCheckedChange: (button: CompoundButton, value: Boolean) -> Unit = { _, _ -> }
)
