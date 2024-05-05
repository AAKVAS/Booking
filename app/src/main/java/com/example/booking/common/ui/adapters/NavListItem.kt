package com.example.booking.common.ui.adapters

import com.example.booking.R

data class NavListItem(
    val title: String = "",
    val description: String = "",
    val imageSrc: Int? = R.drawable.next,
    val onClick: () -> Unit = {},
)
