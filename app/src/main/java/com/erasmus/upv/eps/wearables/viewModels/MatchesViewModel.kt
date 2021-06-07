package com.erasmus.upv.eps.wearables.viewModels

import androidx.lifecycle.*
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.model.MatchWithTeams
import com.erasmus.upv.eps.wearables.repositories.MatchRepository
import com.erasmus.upv.eps.wearables.repositories.StatisticsRepository
import com.erasmus.upv.eps.wearables.util.Sports
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MatchesViewModel
@Inject constructor(
    private val repository: MatchRepository,
    private val statsRepo: StatisticsRepository
) : ViewModel(){

    var filterSport: String = Sports.FOOTBALL
    var matchId: Long = 0L

    fun getAllUpcomingMatches(): LiveData<List<MatchWithTeams>>{
        return repository.upcomingMatches.asLiveData()
    }

    fun getAllPastMatches(): LiveData<List<MatchWithTeams>>{
        return repository.pastMatches.asLiveData()
    }

    fun getInfoAboutTheMatch(): LiveData<MatchWithTeams>{
        return repository.getMatchAndTeamsById(matchId)
    }

    fun deleteMatch(id: Long = matchId) {
        viewModelScope.launch {
            repository.deleteMatchById(id)
            repository.deleteMatchTeamCrossRefByMatchId(id)
            statsRepo.deleteLiveActionsByMatchId(id)
        }
    }


}