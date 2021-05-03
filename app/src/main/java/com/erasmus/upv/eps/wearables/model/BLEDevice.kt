package com.erasmus.upv.eps.wearables.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class BLEDevice(
        @PrimaryKey
        val address: String,
        val name: String,
)