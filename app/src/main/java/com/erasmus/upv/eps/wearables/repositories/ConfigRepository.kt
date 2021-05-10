package com.erasmus.upv.eps.wearables.repositories

import androidx.lifecycle.LiveData
import com.erasmus.upv.eps.wearables.db.dao.ConfigDao
import com.erasmus.upv.eps.wearables.model.BLEDevice
import com.erasmus.upv.eps.wearables.model.BLEDeviceWithGestures
import com.erasmus.upv.eps.wearables.model.Gesture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ConfigRepository
constructor(private val configDao: ConfigDao){



//    suspend fun insertBLEDevice(bleDevice: List<BLEDevice>){
//        withContext(Dispatchers.IO){
//            configDao.insertBLEDevicesConfig(bleDevice)
//        }
//    }

    suspend fun insertGesturesConfig(gestures: List<Gesture>){
        withContext(Dispatchers.IO){
            configDao.insertGestures(gestures)
        }
    }

    suspend fun getBLEDevicesWithGestures(): List<BLEDeviceWithGestures> {
        return withContext(Dispatchers.IO) {
            configDao.getBLEDevicesWithGestures()
        }
    }

    suspend fun insertBLEDeviceWithGesture(devicesWithGestures: MutableList<BLEDeviceWithGestures>) {
        withContext(Dispatchers.IO){
            devicesWithGestures.forEach {
                if(!configDao.isDeviceInDatabase(it.bleDevice.address)){
                    insertBLEDevice(it.bleDevice)
                    insertGesturesConfig(it.gestures)
                }else{
                    insertGesturesConfig(it.gestures)
                }
            }
        }
    }

    private suspend fun insertBLEDevice(bleDevice: BLEDevice) {
        withContext(Dispatchers.IO) {
            configDao.insertBLEDeviceConfig(bleDevice)
        }
    }

    suspend fun deleteAllDevicesAndGestures() {
        withContext(Dispatchers.IO){
            configDao.deleteAllDevices()
            configDao.deleteAllGestures()
        }
    }


}