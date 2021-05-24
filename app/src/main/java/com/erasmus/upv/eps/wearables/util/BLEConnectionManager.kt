package com.erasmus.upv.eps.wearables.util

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import androidx.lifecycle.MutableLiveData
import com.erasmus.upv.eps.wearables.model.Response
import timber.log.Timber
import java.util.*
import kotlin.collections.HashMap

object BLEConnectionManager {




    private val bluetoothGatts = HashMap<String, BluetoothGatt?>()
    val isConnectionChanged = MutableLiveData<Boolean>(false)


    fun getBluetoothConnectedGatts(): List<BluetoothGatt?>{
        return bluetoothGatts.values.toList()
    }


    var responseList: MutableLiveData<MutableList<Response>> = MutableLiveData(emptyList<Response>().toMutableList())


    val gattCallback = object : BluetoothGattCallback(){
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            val deviceAddress = gatt?.device?.address
            val deviceName = gatt?.device?.name
            if(status == BluetoothGatt.GATT_SUCCESS){
                if(newState == BluetoothGatt.STATE_CONNECTED){
                    Timber.i("onConnectionStateChange: connected address $deviceAddress name = $deviceName")

                    // save device instance
                   // bluetoothGatt = gatt
                    if(gatt != null && !bluetoothGatts.containsKey(gatt.device?.address)){
                        bluetoothGatts[gatt.device.address] = gatt
            //            bluetoothGatts[gatt.device.address]?.discoverServices()
                    }
                }else if (newState == BluetoothGatt.STATE_DISCONNECTED){
                    Timber.w("onConnectionStateChange: disconnected address $deviceAddress name = $deviceName\"")
                    bluetoothGatts.clear()
                    gatt?.close()
                }
            }else{
                Timber.e("onConnectionStateChange: error $status")
                bluetoothGatts.clear()
                gatt?.close()
            }

            isConnectionChanged.postValue(true)
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            Timber.i( "onServicesDiscovered: discovered ${gatt?.device?.address}, ${gatt?.device?.name}")
            configureReceivingBatteryLevel()
        }



        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            super.onCharacteristicChanged(gatt, characteristic)
            with(characteristic) {
                Timber.i( "onCharacteristicChanged: battery percentage = ${value.first().toInt()} ")
                val batteryLevel = value.first().toInt()
                val result = Response(System.currentTimeMillis(), batteryLevel, gatt.device)
                addResultToLiveDataList(result)
            }
        }
    }

    fun discoverServicesConnectedDevices(){
        bluetoothGatts.forEach { (_, device) ->
            device?.discoverServices()
        }
    }



    private fun addResultToLiveDataList(result: Response) {
        val tempList = responseList.value
        tempList?.add(0, result)
        responseList.postValue(tempList)
    }


    private fun configureReceivingBatteryLevel() {
        val batteryServiceUuid = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb")
        val batteryLevelCharUuid = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb")
        for(gatt in bluetoothGatts.values){
            gatt?.printGattTable()
            val batteryLevelChar = gatt?.getService(batteryServiceUuid)?.getCharacteristic(batteryLevelCharUuid)
            enableNotifications(batteryLevelChar!!, gatt)
        }
    }





    private val CCC_DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb"

    private fun enableNotifications(characteristic: BluetoothGattCharacteristic, bluetoothGatt: BluetoothGatt?) {
        val cccdUuid = UUID.fromString(CCC_DESCRIPTOR_UUID)
        val payload = when {
            characteristic.isIndicatable() -> BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
            characteristic.isNotifiable() -> BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            else -> {
                Timber.e( "${characteristic.uuid} doesn't support notifications")
                return
            }
        }

        characteristic.getDescriptor(cccdUuid)?.let { cccDescriptor ->
            if (bluetoothGatt?.setCharacteristicNotification(characteristic, true) == false) {
                Timber.e( "setCharacteristicNotification failed for ${characteristic.uuid}")
                return
            }
            writeDescriptor(cccDescriptor, payload, bluetoothGatt)
        } ?: Timber.e("${characteristic.uuid} doesn't contain the CCC descriptor!")
    }


    private fun writeDescriptor(descriptor: BluetoothGattDescriptor, payload: ByteArray, bluetoothGatt: BluetoothGatt?) {
        bluetoothGatt?.let { gatt ->
            descriptor.value = payload
            gatt.writeDescriptor(descriptor)
        } ?: error("Not connected to a BLE device!")
    }

    private fun BluetoothGattCharacteristic.isNotifiable(): Boolean =
        containsProperty(BluetoothGattCharacteristic.PROPERTY_NOTIFY)


    private fun BluetoothGattCharacteristic.containsProperty(property: Int): Boolean{
        return properties and property != 0
    }


    private fun BluetoothGattCharacteristic.isIndicatable(): Boolean =
        containsProperty(BluetoothGattCharacteristic.PROPERTY_INDICATE)



    private fun BluetoothGatt.printGattTable() {
        if (services.isEmpty()) {
            Timber.d( "No service and characteristic available, call discoverServices() first?")
            return
        }
        services.forEach { service ->
            val characteristicsTable = service.characteristics.joinToString(
                separator = "\n|--",
                prefix = "|--"
            ) { it.uuid.toString() }
            Timber.d( "\nService ${service.uuid}\nCharacteristics:\n$characteristicsTable"
            )
        }
    }

}