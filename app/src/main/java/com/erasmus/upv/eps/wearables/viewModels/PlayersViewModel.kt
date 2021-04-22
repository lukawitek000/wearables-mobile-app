package com.erasmus.upv.eps.wearables.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.repositories.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel
@Inject constructor(
    private val repository: PlayerRepository
) : ViewModel(){

    var players = emptyList<Player>()

    fun getAllPlayers(): LiveData<List<Player>>{
        return repository.allPlayers.asLiveData()
    }



}