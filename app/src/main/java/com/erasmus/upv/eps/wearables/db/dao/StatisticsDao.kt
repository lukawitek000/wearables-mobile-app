package com.erasmus.upv.eps.wearables.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.erasmus.upv.eps.wearables.model.LiveAction
import kotlinx.coroutines.flow.Flow


@Dao
interface StatisticsDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLiveAction(liveAction: LiveAction): Long

    @Query("SELECT * FROM LiveAction WHERE matchId == :matchId")
    fun getLiveActionsForTheMatch(matchId: Long): Flow<List<LiveAction>>

    @Query("DELETE FROM LiveAction WHERE id == :id")
    suspend fun deleteLiveActionById(id: Long)

}