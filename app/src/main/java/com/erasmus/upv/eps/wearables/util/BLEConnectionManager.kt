package com.erasmus.upv.eps.wearables.util

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.erasmus.upv.eps.wearables.model.Response
import java.util.*

object BLEConnectionManager {

    private var bluetoothGatt: BluetoothGatt? = null
    private const val TAG = "ScanningBLEManager"

    var responseList: MutableLiveData<MutableList<Response>> = MutableLiveData(emptyList<Response>().toMutableList())


    val gattCallback = object : BluetoothGattCallback(){
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            val deviceAddress = gatt?.device?.address
            val deviceName = gatt?.device?.name
            if(status == BluetoothGatt.GATT_SUCCESS){
                if(newState == BluetoothGatt.STATE_CONNECTED){
                    Log.i(TAG, "onConnectionStateChange: connected address $deviceAddress name = $deviceName")

                    // save device instance
                    bluetoothGatt = gatt

                    // gatt?.device?.createBond()
                    // listenToBondStateChanges(requireContext())

                    bluetoothGatt?.discoverServices()
                    //Handler(Looper.getMainLooper()).post {
                    //  bluetoothGatt?.discoverServices()
                    //}
                }else if (newState == BluetoothGatt.STATE_DISCONNECTED){
                    Log.i(TAG, "onConnectionStateChange: disconnected address $deviceAddress name = $deviceName\"")
                    gatt?.close()
                }
            }else{
                Log.i(TAG, "onConnectionStateChange: error $status")
                gatt?.close()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            Log.i(TAG, "onServicesDiscovered: discovered ${gatt?.device?.address}, ${gatt?.device?.name}")
            gatt?.printGattTable()
            readBatteryLevel()
        }


        override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic, status: Int) {
            super.onCharacteristicRead(gatt, characteristic, status)
            when(status){
                BluetoothGatt.GATT_SUCCESS -> {
                    Log.i(TAG, "onCharacteristicRead: array size ${characteristic.value.size}")
                    Log.i(TAG, "onCharacteristicRead: ${characteristic.uuid} ${characteristic.value.toHexString()}")
                    val batteryLevel = characteristic.value.first().toInt()
                    Log.i(TAG, "onCharacteristicRead: battery level $batteryLevel%")

                    val result = Response(System.currentTimeMillis(), batteryLevel)
                    //responseList.value?.add(result)
                    addResultToLiveDataList(result)
                }
                BluetoothGatt.GATT_READ_NOT_PERMITTED -> {
                    Log.i(TAG, "onCharacteristicRead: Read not permitted for ${characteristic.uuid}")
                }
                else -> {
                    Log.i(TAG, "onCharacteristicRead: failed ${characteristic.uuid} error $status")
                }
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic) {
            super.onCharacteristicChanged(gatt, characteristic)
            with(characteristic) {
                Log.i(TAG, "Characteristic $uuid changed | value: ${value.toHexString()}")
                Log.i(TAG, "onCharacteristicChanged: battery percentage = ${value.first().toInt()} ")
                val batteryLevel = value.first().toInt()
                val result = Response(System.currentTimeMillis(), batteryLevel)
                addResultToLiveDataList(result)
                //responseList.value?.add(result)
                //responseList.value = res
            }
        }
    }

    private fun addResultToLiveDataList(result: Response) {
        val tempList = responseList.value
        tempList?.add(result)
        responseList.postValue(tempList)
    }

    fun ByteArray.toHexString(): String =
        joinToString(separator = " ", prefix = "0x") { String.format("%02X", it) }



    private fun BluetoothGatt.printGattTable() {
        if (services.isEmpty()) {
            Log.i(TAG, "No service and characteristic available, call discoverServices() first?")
            return
        }
        services.forEach { service ->
            val characteristicsTable = service.characteristics.joinToString(
                separator = "\n|--",
                prefix = "|--"
            ) { it.uuid.toString() }
            Log.i(TAG, "\nService ${service.uuid}\nCharacteristics:\n$characteristicsTable"
            )
        }
    }


    private fun readBatteryLevel() {
        val batteryServiceUuid = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb")
        val batteryLevelCharUuid = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb")
        val batteryLevelChar = bluetoothGatt?.getService(batteryServiceUuid)?.getCharacteristic(batteryLevelCharUuid)
        Log.i(TAG, "readBatteryLevel: ${batteryLevelChar?.isReadable()}")
        if (batteryLevelChar?.isReadable() == true) {
            bluetoothGatt?.readCharacteristic(batteryLevelChar)
        }
        enableNotifications(batteryLevelChar!!)
    }


    private fun BluetoothGattCharacteristic.isReadable(): Boolean =
        containsProperty(BluetoothGattCharacteristic.PROPERTY_READ)


    private fun BluetoothGattCharacteristic.containsProperty(property: Int): Boolean{
        return properties and property != 0
    }



    private val CCC_DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb"

    fun enableNotifications(characteristic: BluetoothGattCharacteristic) {
        val cccdUuid = UUID.fromString(CCC_DESCRIPTOR_UUID)
        val payload = when {
            characteristic.isIndicatable() -> BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
            characteristic.isNotifiable() -> BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            else -> {
                Log.e(TAG, "${characteristic.uuid} doesn't support notifications/indications")
                return
            }
        }

        characteristic.getDescriptor(cccdUuid)?.let { cccDescriptor ->
            if (bluetoothGatt?.setCharacteristicNotification(characteristic, true) == false) {
                Log.e(TAG, "setCharacteristicNotification failed for ${characteristic.uuid}")
                return
            }
            writeDescriptor(cccDescriptor, payload)
        } ?: Log.e(TAG, "${characteristic.uuid} doesn't contain the CCC descriptor!")
    }


    fun disableNotifications(characteristic: BluetoothGattCharacteristic) {
        if (!characteristic.isNotifiable() && !characteristic.isIndicatable()) {
            Log.e(TAG, "${characteristic.uuid} doesn't support indications/notifications")
            return
        }

        val cccdUuid = UUID.fromString(CCC_DESCRIPTOR_UUID)
        characteristic.getDescriptor(cccdUuid)?.let { cccDescriptor ->
            if (bluetoothGatt?.setCharacteristicNotification(characteristic, false) == false) {
                Log.e(TAG, "setCharacteristicNotification failed for ${characteristic.uuid}")
                return
            }
            writeDescriptor(cccDescriptor, BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE)
        } ?: Log.e(TAG, "${characteristic.uuid} doesn't contain the CCC descriptor!")
    }


    fun writeDescriptor(descriptor: BluetoothGattDescriptor, payload: ByteArray) {
        bluetoothGatt?.let { gatt ->
            descriptor.value = payload
            gatt.writeDescriptor(descriptor)
        } ?: error("Not connected to a BLE device!")
    }

    fun BluetoothGattCharacteristic.isNotifiable(): Boolean =
        containsProperty(BluetoothGattCharacteristic.PROPERTY_NOTIFY)

    fun BluetoothGattCharacteristic.isIndicatable(): Boolean =
        containsProperty(BluetoothGattCharacteristic.PROPERTY_INDICATE)

    private fun BluetoothGattDescriptor.containsPermission(permission: Int): Boolean =
        permissions and permission != 0






}