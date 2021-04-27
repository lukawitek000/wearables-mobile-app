package com.erasmus.upv.eps.wearables.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.model.TeamWithPlayers
import com.erasmus.upv.eps.wearables.repositories.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel
@Inject constructor(
    private val repository: TeamRepository
): ViewModel(){

    var teamId: Long = 0L

    fun getAllTeams(): LiveData<List<Team>>{
        return repository.allTeams.asLiveData()
    }

    fun getTeamDetailInfo(id: Long): LiveData<TeamWithPlayers>{
        return repository.getTeamWithPlayers(id)
    }

    fun deleteTeamById(teamId: Long) {
        viewModelScope.launch {
            repository.deleteTeamById(teamId)
        }
    }


}