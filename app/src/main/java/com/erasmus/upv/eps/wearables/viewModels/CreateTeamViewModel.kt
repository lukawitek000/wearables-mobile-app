package com.erasmus.upv.eps.wearables.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.repositories.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTeamViewModel
@Inject constructor(
    private val repository: TeamRepository
) : ViewModel(){

    val teamId = MutableLiveData<Long>()

    fun saveTeam(team: Team) {
        Log.i("CreateTeamViewModel", "saveTeam: $team ")
        viewModelScope.launch {
             teamId.value = repository.saveTeam(team)
        }
    }

}