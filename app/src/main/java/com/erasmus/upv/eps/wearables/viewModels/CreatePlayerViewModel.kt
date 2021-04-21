package com.erasmus.upv.eps.wearables.viewModels

import androidx.lifecycle.ViewModel
import com.erasmus.upv.eps.wearables.model.Player

class CreatePlayerViewModel : ViewModel() {


    val player = Player(0L, "", "", 0, "")

    fun setPlayersSport(sport: String){
        player.sport = sport
    }


    fun createPlayer(name: String, number: Int, position: String, otherInfo: String){
        player.id = 0L
        player.name = name
        player.number = number
        player.position = position
        player.otherInfo = otherInfo
    }


}