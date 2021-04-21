package com.erasmus.upv.eps.wearables.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.erasmus.upv.eps.wearables.db.dao.PlayerDao
import com.erasmus.upv.eps.wearables.model.Player


@Database(entities = [Player::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao

    companion object{
        fun createAppDatabase(context: Context): AppDatabase{
            return Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "Wearables-Database"
            ).build()
        }
    }

}