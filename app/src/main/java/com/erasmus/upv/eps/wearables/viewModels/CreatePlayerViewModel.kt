package com.erasmus.upv.eps.wearables.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.repositories.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePlayerViewModel
@Inject constructor(
        private val repository: PlayerRepository
    ): ViewModel() {

    var player = Player()


    fun getPlayerById(id: Long): LiveData<Player>{
        return repository.getPlayerById(id)
    }


    fun createPlayer(name: String, number: Int, nationality: String, position: String, otherInfo: String){
        setPlayerProperties(name, number, nationality, position, otherInfo)
        savePlayerToDatabase()
    }


    private fun setPlayerProperties(name: String, number: Int, nationality: String, position: String, otherInfo: String) {
        player.name = name
        player.number = number
        player.nationality = nationality
        player.position = position
        player.otherInfo = otherInfo
    }


    private fun savePlayerToDatabase() {
        viewModelScope.launch {
            repository.insertPlayer(player)
        }
    }


    fun updatePlayer(name: String, playerNumber: Int, nationality: String, position: String, otherInfo: String) {
        viewModelScope.launch {
            setPlayerProperties(name, playerNumber, nationality, position, otherInfo)
            repository.updatePlayer(player)
        }
    }


    fun isNewPlayerCreated(): Boolean {
        return player.playerId == 0L
    }

}