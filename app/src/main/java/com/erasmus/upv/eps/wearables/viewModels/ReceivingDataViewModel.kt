package com.erasmus.upv.eps.wearables.viewModels

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erasmus.upv.eps.wearables.model.BLEDevice
import com.erasmus.upv.eps.wearables.model.BLEDeviceWithGestures
import com.erasmus.upv.eps.wearables.model.Gesture

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

    fun getSelectedBLEDevices(): List<BLEDevice>{
        return selectedScanResults.map {
            createBLEDevice(it)
        }
    }

    private fun createBLEDevice(it: BluetoothDevice): BLEDevice {
        var name = it.name
        if (name == null) {
            name = "Unknown Device"
        }
        return BLEDevice(it.address, name)
    }

    fun getSelectedBLEDevicesWithGestures(): List<BLEDeviceWithGestures> {
        return selectedScanResults.map {
            BLEDeviceWithGestures(
                    createBLEDevice(it),
                    getDefaultGesturesListForDevice(it.address)
            )
        }
    }

    private fun getDefaultGesturesListForDevice(deviceId: String): List<Gesture>{
        val gestures =  mutableListOf<Gesture>()
        for (i in 1..10){
            gestures.add(
                    Gesture(0L, "Gesture $i", i, null, 0L, 0L, deviceId)
            )
        }
        return gestures
    }


}

