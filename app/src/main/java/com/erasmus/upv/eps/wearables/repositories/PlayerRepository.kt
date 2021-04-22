package com.erasmus.upv.eps.wearables.repositories

import android.content.Context
import com.erasmus.upv.eps.wearables.db.AppDatabase
import com.erasmus.upv.eps.wearables.db.dao.PlayerDao
import com.erasmus.upv.eps.wearables.model.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlayerRepository
    constructor(
        private val playerDao: PlayerDao
        ) {


    suspend fun savePlayer(player: Player){
        withContext(Dispatchers.IO) {
            playerDao.insertPlayer(player)
        }
    }


}