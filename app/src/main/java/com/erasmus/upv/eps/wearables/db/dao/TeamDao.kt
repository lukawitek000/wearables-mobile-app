package com.erasmus.upv.eps.wearables.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.erasmus.upv.eps.wearables.model.Team

@Dao
interface TeamDao {


    @Insert
    suspend fun insertTeam(team: Team): Long


}