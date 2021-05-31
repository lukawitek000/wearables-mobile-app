package com.erasmus.upv.eps.wearables.db

import android.bluetooth.BluetoothClass
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.erasmus.upv.eps.wearables.db.dao.*
import com.erasmus.upv.eps.wearables.db.util.Converters
import com.erasmus.upv.eps.wearables.model.*


@Database(entities = [Player::class, Team::class, Match::class, MatchTeamCrossRef::class, BLEDevice::class,
                     Gesture::class, LiveAction::class], version = 15, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao
    abstract fun teamDao(): TeamDao
    abstract fun matchDao(): MatchDao
    abstract fun configDao(): ConfigDao
    abstract fun statisticsDao(): StatisticsDao
}