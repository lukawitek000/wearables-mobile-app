package com.erasmus.upv.eps.wearables.viewModels

import androidx.lifecycle.*
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.model.MatchWithTeams
import com.erasmus.upv.eps.wearables.repositories.MatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MatchesViewModel
@Inject constructor(
    private val repository: MatchRepository
) : ViewModel(){

    var matchId: Long = 0L

    fun getAllUpcomingMatches(): LiveData<List<Match>>{
        return repository.allMatches.asLiveData()
    }

    fun getAllPastMatches(): LiveData<List<Match>>{
        return repository.allMatches.asLiveData()
    }

    fun getInfoAboutTheMatch(): LiveData<MatchWithTeams>{
        return repository.getMatchAndTeamsById(matchId)
    }

    fun deleteMatch() {
        viewModelScope.launch {
            repository.deleteMatchById(matchId)
            repository.deleteMatchTeamCrossRefByMatchId(matchId)
        }
    }


}