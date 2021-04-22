package com.erasmus.upv.eps.wearables.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.erasmus.upv.eps.wearables.model.Match
import kotlinx.coroutines.flow.Flow


@Dao
interface MatchDao {

    @Insert
    suspend fun insertMatch(match: Match): Long

    @Query("SELECT * FROM `Match`")
    fun getAllMatches(): Flow<List<Match>>

}