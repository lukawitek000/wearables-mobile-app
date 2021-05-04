package com.erasmus.upv.eps.wearables.repositories

import com.erasmus.upv.eps.wearables.db.dao.ConfigDao
import com.erasmus.upv.eps.wearables.model.BLEDevice
import com.erasmus.upv.eps.wearables.model.BLEDeviceWithGestures
import com.erasmus.upv.eps.wearables.model.Gesture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ConfigRepository
constructor(private val configDao: ConfigDao){



    suspend fun insertBLEDevice(bleDevice: List<BLEDevice>){
        withContext(Dispatchers.IO){
            configDao.insertBLEDevicesConfig(bleDevice)
        }
    }

    suspend fun insertGesturesConfig(gestures: List<Gesture>){
        withContext(Dispatchers.IO){
            configDao.insertGestures(gestures)
        }
    }


}