package com.erasmus.upv.eps.wearables.db.dao

import androidx.room.*
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.model.TeamWithPlayers
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(team: Team): Long

    @Query("SELECT * FROM Team")
    fun getAllTeams(): Flow<List<Team>>

    @Transaction
    @Query("SELECT * FROM Team WHERE teamId == :teamId")
    fun getTeamWithPlayers(teamId: Long) : TeamWithPlayers

}