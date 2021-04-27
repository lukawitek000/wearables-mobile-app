package com.erasmus.upv.eps.wearables.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.model.MatchTeamCrossRef
import com.erasmus.upv.eps.wearables.model.MatchWithTeams
import kotlinx.coroutines.flow.Flow


@Dao
interface MatchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: Match): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatchTeamCrossRef(matchTeamCrossRef: MatchTeamCrossRef): Long


    @Query("SELECT * FROM `Match`")
    fun getAllMatches(): Flow<List<Match>>


    @Query("SELECT * FROM `Match` WHERE :id == matchId")
    fun getMatchById(id: Long): LiveData<Match>

    @Transaction
    @Query("SELECT * FROM `Match` WHERE :id == matchId")
    fun getMatchWithTeams(id: Long): LiveData<MatchWithTeams>

    @Query("DELETE FROM `Match` WHERE matchId == :matchId")
    fun deleteMatchById(matchId: Long)

    @Query("DELETE FROM MatchTeamCrossRef WHERE matchId == :matchId")
    fun deleteMatchTeamCrossRefByMatchId(matchId: Long)

}