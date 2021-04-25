package com.erasmus.upv.eps.wearables.viewModels

import androidx.lifecycle.SavedStateHandle
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
        private val repository: PlayerRepository,
        private val savedStateHandle: SavedStateHandle
    ): ViewModel() {


    val player = Player(0L, 0L, "", "", 0, "")

    fun setPlayersSport(sport: String){
        player.sport = sport
    }


    fun createPlayer(name: String, number: Int, position: String, otherInfo: String){
        player.playerId = 0L
        player.name = name
        player.number = number
        player.position = position
        player.otherInfo = otherInfo
        savePlayerToDatabase()
    }

    private fun savePlayerToDatabase() {
        viewModelScope.launch {
            repository.savePlayer(player)
        }
    }


}