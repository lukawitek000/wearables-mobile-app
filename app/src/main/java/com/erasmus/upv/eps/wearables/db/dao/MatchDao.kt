package com.erasmus.upv.eps.wearables.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.model.MatchWithTeams
import kotlinx.coroutines.flow.Flow


@Dao
interface MatchDao {

    @Insert
    suspend fun insertMatch(match: Match): Long

    @Query("SELECT * FROM `Match`")
    fun getAllMatches(): Flow<List<Match>>


    @Query("SELECT * FROM `Match` WHERE :id == matchId")
    fun getMatchById(id: Long): LiveData<Match>

    @Transaction
    @Query("SELECT * FROM `Match` WHERE :id == matchId")
    fun getMatchWithTeams(id: Long): LiveData<MatchWithTeams>

}