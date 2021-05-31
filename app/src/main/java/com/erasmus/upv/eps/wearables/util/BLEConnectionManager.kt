package com.erasmus.upv.eps.wearables.util

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import androidx.lifecycle.MutableLiveData
import com.erasmus.upv.eps.wearables.model.Response
import com.erasmus.upv.eps.wearables.service.BLEConnectionForegroundService
import timber.log.Timber
import java.util.*
import kotlin.collections.HashMap

object BLEConnectionManager {

    enum class GattStatus{
        CONNECTING,
        DISCONNECTED,
        CONNECTED
    }


    private val bluetoothGatts = HashMap<String, BluetoothGatt?>()
    val bluetoothGattsStatus = MutableLiveData<HashMap<String, GattStatus>>()
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
                    if(gatt != null && !bluetoothGatts.containsKey(deviceAddress)){
                        bluetoothGatts[deviceAddress!!] = gatt
                        BLEConnectionForegroundService.gattDevicesMap[deviceAddress] = gatt
                        updateGattStatus(deviceAddress, GattStatus.CONNECTED)
            //            bluetoothGatts[gatt.device.address]?.discoverServices()
                    }
                }else if (newState == BluetoothGatt.STATE_DISCONNECTED){
                    Timber.w("onConnectionStateChange: disconnected address $deviceAddress name = $deviceName\"")
                    //bluetoothGatts.clear()
                    if (deviceAddress != null) {
                        updateGattStatus(deviceAddress, GattStatus.DISCONNECTED)
                    }
                    bluetoothGatts.remove(gatt?.device?.address)
                    gatt?.close()
                }
            }else{
                Timber.e("onConnectionStateChange: error $status")
                //bluetoothGatts.clear()
                bluetoothGatts.remove(gatt?.device?.address)
                if (deviceAddress != null) {
                    updateGattStatus(deviceAddress, GattStatus.DISCONNECTED)
                }
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
            Timber.d("characteristic changed ${gatt.device.name} ch ${characteristic.uuid}")
            with(characteristic) {
                Timber.i( "onCharacteristicChanged: battery percentage = $value ")
                val batteryLevel = value.first().toInt()
                val result = Response(System.currentTimeMillis(), batteryLevel, gatt.device)
                addResultToLiveDataList(result)
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            with(characteristic) {
                when (status) {
                    BluetoothGatt.GATT_SUCCESS -> {
                        Timber.i("Read characteristic $uuid:\n${value.toHexString()}")
                    }
                    BluetoothGatt.GATT_READ_NOT_PERMITTED -> {
                        Timber.e("Read not permitted for $uuid!")
                    }
                    else -> {
                        Timber.e( "Characteristic read failed for $uuid, error: $status")
                    }
                }
            }
        }
    }
    fun ByteArray.toHexString(): String =
        joinToString(separator = " ", prefix = "0x") { String.format("%02X", it) }


    fun discoverServicesConnectedDevices(){
        bluetoothGatts.forEach { (_, device) ->
            device?.discoverServices()
        }
    }



    private fun addResultToLiveDataList(result: Response) {
        val tempList = responseList.value
        tempList?.add(0, result)
        responseList.postValue(tempList!!)
    }

//    2021-05-26 15:19:54.240 27997-28016/com.erasmus.upv.eps.wearables D/BLEConnectionManager: Service 4fafc201-1fb5-459e-8fcc-c5c9c331914b
//    Characteristics:
//    |--beb5483e-36e1-4688-b7f5-ea07361b26a8

    private fun configureReceivingBatteryLevel() {
//        val batteryServiceUuid = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb")
//        val batteryLevelCharUuid = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb")
        val batteryServiceUuid = UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b")
        val batteryLevelCharUuid = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8")
        for(gatt in bluetoothGatts.values){
            gatt?.printGattTable()
            val batteryLevelChar = gatt?.getService(batteryServiceUuid)?.getCharacteristic(batteryLevelCharUuid)
            Timber.d("characteristic ${batteryLevelChar?.uuid}")
            enableNotifications(batteryLevelChar!!, gatt)
            Timber.d("characteristic 2 ${batteryLevelChar?.uuid}")
        }
    }


    fun readGesture(){
        val batteryServiceUuid = UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b")
        val batteryLevelCharUuid = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8")
        for(gatt in bluetoothGatts.values){
            gatt?.printGattTable()
            val batteryLevelChar = gatt?.getService(batteryServiceUuid)?.getCharacteristic(batteryLevelCharUuid)
            if (batteryLevelChar?.isReadable() == true) {
                gatt.readCharacteristic(batteryLevelChar)
            }
        }
    }

    private fun BluetoothGattCharacteristic.isReadable(): Boolean =
        containsProperty(BluetoothGattCharacteristic.PROPERTY_READ)




    private val CCC_DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb"
//    private val CCC_DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb"

    private fun enableNotifications(characteristic: BluetoothGattCharacteristic, bluetoothGatt: BluetoothGatt?) {
        Timber.d("enable notifications with ${characteristic.uuid.toString()}  $bluetoothGatt")


        val descriptors = characteristic.descriptors.map { it.uuid.toString() }
        Timber.d("descriptors $descriptors")

        val cccdUuid = UUID.fromString(CCC_DESCRIPTOR_UUID)
        val payload = when {
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


    fun updateGattStatus(address: String, status: GattStatus){
        Timber.d("update connection status address: $address status: ${status.name}")
        val tempMap = bluetoothGattsStatus.value ?: HashMap<String, GattStatus>()
        tempMap[address] = status
        bluetoothGattsStatus.postValue(tempMap)
    }

    fun disconnectAllDevices() {
        bluetoothGatts.values.forEach {
            it?.disconnect()
        }
        BLEConnectionForegroundService.gattDevicesMap.clear()
    }


}