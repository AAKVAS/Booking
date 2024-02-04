package com.example.booking.common.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class BaseEntity(
    @PrimaryKey(autoGenerate = false)
    val uid: String
)