package com.erasmus.upv.eps.wearables.model

import androidx.room.Embedded
import androidx.room.Relation


data class BLEDeviceWithGestures(
        @Embedded
        val bleDevice: BLEDevice,
        @Relation(
                parentColumn = "address",
                entityColumn = "bleDeviceId",
        )
        val gestures: List<Gesture>
)