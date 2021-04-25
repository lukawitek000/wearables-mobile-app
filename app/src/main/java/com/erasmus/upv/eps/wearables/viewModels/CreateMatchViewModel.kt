package com.erasmus.upv.eps.wearables.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.repositories.MatchRepository
import com.erasmus.upv.eps.wearables.repositories.PlayerRepository
import com.erasmus.upv.eps.wearables.repositories.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.security.interfaces.RSAMultiPrimePrivateCrtKey
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateMatchViewModel
    @Inject constructor(private val repository: MatchRepository,
    private val playerRepository: PlayerRepository): ViewModel() {


    var matchDate: Calendar = Calendar.getInstance()

    var match: Match? = null
    var homeTeam: Team? = null
    var guestTeam: Team? = null

    var isCreatingTeam = false
    val teamPlayers = mutableListOf<Player>()


    fun createMatch(match: Match){
        Log.i("CreateMatchViewModel", "createMatch: $match")
        viewModelScope.launch {
            this@CreateMatchViewModel.match = match
            this@CreateMatchViewModel.match!!.date = Date(matchDate.timeInMillis)
            this@CreateMatchViewModel.match!!.matchId = repository.insertMatch(this@CreateMatchViewModel.match!!)
            createMatchTeamCrossRef()
        }
    }

    fun areBothTeamsAdded(): Boolean {
        return (homeTeam != null && guestTeam != null)
    }

    private suspend fun createMatchTeamCrossRef(){
        if(match != null && homeTeam != null && guestTeam != null) {
            repository.insertMatchTeamCrossRef(match!!.matchId, homeTeam!!.teamId)
            repository.insertMatchTeamCrossRef(match!!.matchId, guestTeam!!.teamId)
        }
    }

    fun updateTeamOfPlayers(teamId: Long) {
        viewModelScope.launch {
            teamPlayers.forEach {
                it.teamOfPlayerId = teamId
                playerRepository.updatePlayer(it)
            }
        }
    }


}