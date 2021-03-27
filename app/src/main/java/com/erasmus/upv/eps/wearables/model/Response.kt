package com.erasmus.upv.eps.wearables.model

import android.bluetooth.BluetoothDevice

data class Response(
    var timeStamp: Long,
    var data: Int,
    var device: BluetoothDevice
)