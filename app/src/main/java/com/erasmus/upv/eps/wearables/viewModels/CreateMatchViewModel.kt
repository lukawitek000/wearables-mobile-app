package com.erasmus.upv.eps.wearables.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.repositories.MatchRepository
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
    private val teamRepository: TeamRepository): ViewModel() {


    var matchDate: Calendar = Calendar.getInstance()

    var match: Match? = null
    var homeTeam: Team? = null
    var guestTeam: Team? = null

    val matchId = MutableLiveData<Long>()
    val teamsIds = MutableLiveData<List<Long>>()
    //val guestTeamId = MutableLiveData<Long>()

    fun createMatch(match: Match){
        Log.i("CreateMatchViewModel", "createMatch: $match")
        viewModelScope.launch {
            match.date = Date(matchDate.timeInMillis)
//            val m = async {  matchId.postValue(repository.insertMatch(match)) }
//            val t = async { createTeams() }
//            m.await()
//            t.await()
            matchId.postValue(repository.insertMatch(match))
            createTeams()
            Log.i("CreateMatchViewModel", "createMatch: ${matchId.value} ${teamsIds.value} ${homeTeam?.teamId} ${guestTeam?.teamId}")
            createMatchTeamCrossRef()
        }
    }

    fun areBothTeamsAdded(): Boolean {
        return (homeTeam != null && guestTeam != null)
    }

    suspend fun createMatchTeamCrossRef(){
        //viewModelScope.launch {
            if(match != null && homeTeam != null && guestTeam != null) {
                repository.insertMatchTeamCrossRef(matchId.value!!, homeTeam!!.teamId)
                repository.insertMatchTeamCrossRef(matchId.value!!, guestTeam!!.teamId)
            }
        //}
    }

    suspend fun createTeams() {
        //viewModelScope.launch {
            var homeTeamId = 0L
            var guestTeamId = 0L
            if(homeTeam != null){
               homeTeamId  = teamRepository.saveTeam(homeTeam!!)
            }
            if(guestTeam != null){
                guestTeamId =  teamRepository.saveTeam(guestTeam!!)
            }
            teamsIds.postValue(listOf(homeTeamId, guestTeamId))
        //}

    }


}