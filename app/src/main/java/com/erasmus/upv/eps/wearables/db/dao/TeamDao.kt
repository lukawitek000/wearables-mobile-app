package com.erasmus.upv.eps.wearables.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.erasmus.upv.eps.wearables.model.Team
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {


    @Insert
    suspend fun insertTeam(team: Team): Long

    @Query("SELECT * FROM Team")
    fun getAllTeams(): Flow<List<Team>>

}