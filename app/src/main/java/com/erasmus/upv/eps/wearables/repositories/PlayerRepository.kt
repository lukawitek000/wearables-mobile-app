package com.erasmus.upv.eps.wearables.repositories

import android.content.Context
import com.erasmus.upv.eps.wearables.db.AppDatabase
import com.erasmus.upv.eps.wearables.db.dao.PlayerDao
import com.erasmus.upv.eps.wearables.model.Player

class PlayerRepository
    constructor(
        private val playerDao: PlayerDao
        ) {


    fun savePlayer(player: Player){
        playerDao.insertPlayer(player)
    }


}