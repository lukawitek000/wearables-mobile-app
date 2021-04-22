package com.erasmus.upv.eps.wearables.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.repositories.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel
@Inject constructor(
    private val repository: TeamRepository
): ViewModel(){

    fun getAllTeams(): LiveData<List<Team>>{
        return repository.allTeams.asLiveData()
    }


}