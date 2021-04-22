package com.erasmus.upv.eps.wearables.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.erasmus.upv.eps.wearables.db.AppDatabase
import com.erasmus.upv.eps.wearables.db.dao.PlayerDao
import com.erasmus.upv.eps.wearables.model.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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


    val allPlayers : Flow<List<Player>> = playerDao.getAllPlayers()

}