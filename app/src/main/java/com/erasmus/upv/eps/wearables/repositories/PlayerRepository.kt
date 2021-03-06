package com.erasmus.upv.eps.wearables.repositories

import androidx.lifecycle.LiveData
import com.erasmus.upv.eps.wearables.db.dao.PlayerDao
import com.erasmus.upv.eps.wearables.model.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PlayerRepository
    constructor(
        private val playerDao: PlayerDao
        ) {


    suspend fun insertPlayer(player: Player){
        withContext(Dispatchers.IO) {
            playerDao.insertPlayer(player)
        }
    }

    suspend fun updatePlayer(player: Player) {
        withContext(Dispatchers.IO){
            playerDao.updatePlayer(player)
        }
    }

    suspend fun deletePlayer(player: Player) {
        withContext(Dispatchers.IO){
            playerDao.deletePlayer(player)
        }
    }

    fun getPlayerById(id: Long): LiveData<Player> {
        return playerDao.getPlayerById(id)
    }


    suspend fun resetTeamOfThePlayer(playerId: Long) {
        withContext(Dispatchers.IO){
            playerDao.resetTeamOfThePlayerByPlayerId(playerId)
        }
    }


    val allPlayers : Flow<List<Player>> = playerDao.getAllPlayers()

}