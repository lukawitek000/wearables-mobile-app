package com.erasmus.upv.eps.wearables.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.erasmus.upv.eps.wearables.model.LiveAction


@Dao
interface StatisticsDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLiveAction(liveAction: LiveAction): Long

}