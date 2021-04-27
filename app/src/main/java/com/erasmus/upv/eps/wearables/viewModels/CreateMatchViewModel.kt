package com.erasmus.upv.eps.wearables.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.model.MatchWithTeams
import com.erasmus.upv.eps.wearables.repositories.MatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateMatchViewModel
    @Inject constructor(
    private val repository: MatchRepository
) : ViewModel() {


    val match = Match(0L, Date(), "", "", "", "")

    var receivedMatchId = 0L
    var matchWithTeams = MatchWithTeams(Match(0L, Date(), "", "", "", ""), listOf())

    private val _matchId = MutableLiveData<Long>()
    val matchId: LiveData<Long>
        get() = _matchId

    var matchDate: Calendar = Calendar.getInstance()

    fun insertMatch(){
        viewModelScope.launch {
            finishCreatingMatch()
            _matchId.value = repository.insertMatch(match)
        }
    }

    fun getMatchDetails(): LiveData<MatchWithTeams> {
        return repository.getMatchAndTeamsById(receivedMatchId)
    }

    fun updateMatch() {
        viewModelScope.launch {
            finishCreatingMatch()
            repository.resetTeamsInMatch(match)
           repository.updateMatch(match)
            _matchId.value = receivedMatchId
        }
    }


    private fun finishCreatingMatch() {
        match.date = Date(matchDate.timeInMillis)
        match.matchId = receivedMatchId
    }


}