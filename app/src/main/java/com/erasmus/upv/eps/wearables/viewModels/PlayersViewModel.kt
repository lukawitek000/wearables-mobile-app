package com.erasmus.upv.eps.wearables.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.repositories.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel
@Inject constructor(
    private val repository: PlayerRepository
) : ViewModel(){

    var playerInfo: Player? = null
    var playerId: Long = 0L

    fun getDetailsAboutPlayer(id: Long): LiveData<Player>{
        return repository.getPlayerById(id)
    }


    fun getAllPlayers(): LiveData<List<Player>>{
        return repository.allPlayers.asLiveData()
    }



    fun filterPlayers(players: List<Player>, alreadyAddedPlayers: List<Player>): List<Player>{
        return players.filter { player ->
            player.teamOfPlayerId == 0L && !alreadyAddedPlayers.any {  player.playerId == it.playerId }
        }
    }

    fun deletePlayer(player: Player) {
        viewModelScope.launch {
            repository.deletePlayer(player)
        }
    }

}