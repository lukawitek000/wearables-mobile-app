package com.erasmus.upv.eps.wearables.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.erasmus.upv.eps.wearables.model.Match


@Dao
interface MatchDao {

    @Insert
    suspend fun insertMatch(match: Match): Long

}