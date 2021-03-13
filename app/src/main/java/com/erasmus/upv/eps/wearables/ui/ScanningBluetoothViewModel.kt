package com.erasmus.upv.eps.wearables.ui

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel

class ScanningBluetoothViewModel: ViewModel() {


    var scanResults = mutableListOf<BluetoothDevice>()



}