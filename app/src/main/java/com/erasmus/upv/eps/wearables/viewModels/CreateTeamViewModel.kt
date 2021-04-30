package com.erasmus.upv.eps.wearables.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.model.TeamWithPlayers
import com.erasmus.upv.eps.wearables.repositories.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTeamViewModel
@Inject constructor(
    private val repository: TeamRepository
) : ViewModel(){

    var teamWithPlayers: TeamWithPlayers = TeamWithPlayers(
            Team(),
            listOf())
    val teamId = MutableLiveData<Long>()

    var receivedTeamId: Long = 0L

    fun saveTeam(team: Team) {
        viewModelScope.launch {
             teamId.value = repository.saveTeam(team)
        }
    }

    fun getTeamWithPlayersById(receivedTeamId: Long): LiveData<TeamWithPlayers> {
        return repository.getTeamWithPlayers(receivedTeamId)
    }

    fun updateTeam(team: Team) {
        viewModelScope.launch {
            team.teamId = receivedTeamId
            teamId.value = repository.updateTeam(team)
        }
    }

}