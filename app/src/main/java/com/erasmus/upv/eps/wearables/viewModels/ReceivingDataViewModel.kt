package com.erasmus.upv.eps.wearables.viewModels

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReceivingDataViewModel: ViewModel() {


    private var scanResults = mutableListOf<BluetoothDevice>()

    var selectedScanResults = mutableListOf<BluetoothDevice>()

    val scanResultsLiveData = MutableLiveData<List<BluetoothDevice>>()

    init {
        scanResultsLiveData.value = ArrayList()
    }


    fun addNewScanResult(device: BluetoothDevice){
        scanResults.add(device)
        scanResultsLiveData.value = scanResults
    }

    fun isDeviceAlreadySaved(device: BluetoothDevice): Boolean {
        return scanResults.indexOfFirst { it.address == device.address } != -1
    }

    fun clearScanResults() {
        scanResults.clear()
        scanResultsLiveData.value = scanResults
    }

    fun getChangedElementIndex(device: BluetoothDevice): Int{
        return scanResults.indexOfFirst { it -> device.address == it.address }
    }

}