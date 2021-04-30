package com.erasmus.upv.eps.wearables.viewModels

import androidx.lifecycle.*
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
    val isTeamDeleted = MutableLiveData<Boolean>()

    fun getAllTeams(): LiveData<List<Team>>{
        return repository.allTeams.asLiveData()
    }

    fun getTeamDetailInfo(id: Long): LiveData<TeamWithPlayers>{
        return repository.getTeamWithPlayers(id)
    }

    fun deleteTeamById(teamId: Long) {
        viewModelScope.launch {
            if(repository.isTeamPartOfMatch(teamId)){
                isTeamDeleted.value = false
            }else {
                repository.deleteTeamById(teamId)
                isTeamDeleted.value = true
            }
        }
    }


}