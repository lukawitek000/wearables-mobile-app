package com.erasmus.upv.eps.wearables.viewModels

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel

class ScanningBluetoothViewModel: ViewModel() {


    var scanResults = mutableListOf<BluetoothDevice>()



}