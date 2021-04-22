package com.erasmus.upv.eps.wearables.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.erasmus.upv.eps.wearables.model.Player
import kotlinx.coroutines.flow.Flow


@Dao
interface PlayerDao {


    @Insert
    suspend fun insertPlayer(player: Player): Long


    @Query("SELECT * FROM Player")
    fun getAllPlayers(): Flow<List<Player>>
}