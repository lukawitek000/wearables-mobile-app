package com.erasmus.upv.eps.wearables.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.erasmus.upv.eps.wearables.model.BLEDevice
import com.erasmus.upv.eps.wearables.model.BLEDeviceWithGestures
import com.erasmus.upv.eps.wearables.model.Gesture

@Dao
interface ConfigDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBLEDevicesConfig(bleDevice: List<BLEDevice>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGestures(gestures: List<Gesture>)


    @Transaction
    @Query("SELECT * FROM BLEDevice")
    suspend fun getBLEDevicesWithGestures(): List<BLEDeviceWithGestures>

    @Query("SELECT EXISTS(SELECT * FROM BLEDevice WHERE address = :bleDeviceAddress)")
    fun isDeviceInDatabase(bleDeviceAddress: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBLEDeviceConfig(bleDevice: BLEDevice)

    @Query("DELETE FROM BLEDevice")
    fun deleteAllDevices()

    @Query("DELETE FROM Gesture")
    fun deleteAllGestures()


}