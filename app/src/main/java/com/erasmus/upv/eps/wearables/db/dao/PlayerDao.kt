package com.erasmus.upv.eps.wearables.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.erasmus.upv.eps.wearables.model.Player


@Dao
interface PlayerDao {


    @Insert
    fun insertPlayer(player: Player): Long


}