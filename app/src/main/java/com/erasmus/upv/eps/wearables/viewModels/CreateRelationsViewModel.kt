package com.erasmus.upv.eps.wearables.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.repositories.MatchRepository
import com.erasmus.upv.eps.wearables.repositories.PlayerRepository
import com.erasmus.upv.eps.wearables.util.TeamCreated
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateRelationsViewModel
    @Inject constructor(private val repository: MatchRepository,
    private val playerRepository: PlayerRepository): ViewModel() {


    var homeTeam: Team? = null
    var guestTeam: Team? = null

    var isCreatingTeam = false
    val teamPlayers = mutableListOf<Player>()
    var whichTeamIsCreated = TeamCreated.NONE

    var creatingTeam = Team(0L, "", "", 0, "", "", "")


    fun areBothTeamsAdded(): Boolean {
        return (homeTeam != null && guestTeam != null)
    }

    fun createMatchTeamCrossRef(matchId: Long){
        viewModelScope.launch {
            if (matchId >= 0L && homeTeam != null && guestTeam != null) {
                repository.insertMatchTeamCrossRef(matchId, homeTeam!!.teamId)
                repository.insertMatchTeamCrossRef(matchId, guestTeam!!.teamId)
                clearTeams()
            }
        }
    }

    fun updateTeamOfPlayers(teamId: Long) {
        viewModelScope.launch {
            teamPlayers.forEach {
                it.teamOfPlayerId = teamId
                Log.i("CreateRelationsViewModel", "updateTeamOfPlayers: $it ")
              //  playerRepository.updatePlayersTeamIdFromTeam(teamId)
                playerRepository.updatePlayer(it)
            }
            teamPlayers.clear()
        }
    }

    fun clearTeams() {
        homeTeam = null
        guestTeam = null
        whichTeamIsCreated = TeamCreated.NONE
    }

    fun addPlayersToTeamPlayers(players: List<Player>) {
        for(player in players){
            if(!teamPlayers.contains(player)){
                teamPlayers.add(player)
            }
        }
    }


}