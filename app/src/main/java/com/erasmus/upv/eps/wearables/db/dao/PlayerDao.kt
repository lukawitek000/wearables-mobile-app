package com.erasmus.upv.eps.wearables.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.erasmus.upv.eps.wearables.model.Player
import kotlinx.coroutines.flow.Flow


@Dao
interface PlayerDao {


    @Insert
    suspend fun insertPlayer(player: Player): Long


    @Query("SELECT * FROM Player")
    fun getAllPlayers(): Flow<List<Player>>

    @Update
    fun updatePlayer(player: Player)

    @Delete
    fun deletePlayer(player: Player)

    @Query("SELECT * FROM Player WHERE playerId == :id")
    fun getPlayerById(id: Long): LiveData<Player>

    @Query("UPDATE Player SET teamOfPlayerId = 0 WHERE teamOfPlayerId == :teamId ")
    fun updatePlayersTeamIdFromTeam(teamId: Long)
}