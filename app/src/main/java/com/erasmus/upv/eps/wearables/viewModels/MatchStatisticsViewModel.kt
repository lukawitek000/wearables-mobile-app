package com.erasmus.upv.eps.wearables.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erasmus.upv.eps.wearables.model.*
import com.erasmus.upv.eps.wearables.model.actions.BasketballActions
import com.erasmus.upv.eps.wearables.model.actions.FootballActions
import com.erasmus.upv.eps.wearables.model.actions.HandballActions
import com.erasmus.upv.eps.wearables.repositories.MatchRepository
import com.erasmus.upv.eps.wearables.repositories.StatisticsRepository
import com.erasmus.upv.eps.wearables.repositories.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchStatisticsViewModel
@Inject constructor(
    private val repository: StatisticsRepository,
    private val matchRepo: MatchRepository,
    private val teamRepo: TeamRepository
) : ViewModel(){

    var matchId = 0L

    var match = Match()
    var homeTeam = TeamWithPlayers(Team(), listOf())
    var guestTeam = TeamWithPlayers(Team(), listOf())

    val teamsWithPlayers = MutableLiveData<List<TeamWithPlayers>>()

     fun getMatchWithTeams(): LiveData<MatchWithTeams> {
         return matchRepo.getMatchAndTeamsById(matchId)
     }

    fun getTeamWithPlayers(teams: List<Team>){
        viewModelScope.launch {
            if (teams.size < 2) {
                return@launch
            }
            val homeTeam = teamRepo.getTeamWithPlayersSuspend(teams[0].teamId)
            val guestTeam = teamRepo.getTeamWithPlayersSuspend(teams[1].teamId)
            teamsWithPlayers.value = listOf<TeamWithPlayers>(homeTeam, guestTeam)
        }
    }


    fun getMatchStatistics(matchId: Long): LiveData<List<LiveAction>>{
        return repository.getLiveActionsForTheMatch(matchId)
    }


    fun deleteLiveAction(liveAction: LiveAction) {
        viewModelScope.launch {
            repository.deleteLiveActionById(liveAction.id)
        }
    }


    fun getTeamColor(teamId: Long): Int{
        return if(teamId == homeTeam.team.teamId){
            match.homeTeamColor
        }else{
            match.guestTeamColor
        }
    }


    fun getPlayerNumberById(playerId: Long): String?{
        return when {
            homeTeam.players.count { player -> player.playerId == playerId } > 0 -> {
                homeTeam.players.find { it.playerId == playerId }?.number.toString()
            }
            guestTeam.players.count { player -> player.playerId == playerId } > 0 -> {
                guestTeam.players.find { it.playerId == playerId }?.number.toString()
            }
            else -> {
                null
            }
        }
    }



    fun getTeamNameById(assignTeamId: Long): String? {
        return when (assignTeamId) {
            homeTeam.team.teamId -> {
                homeTeam.team.name
            }
            guestTeam.team.teamId -> {
                guestTeam.team.name
            }
            else -> {
                null
            }
        }
    }

    fun setMatchAndTeams(matchWithTeams: MatchWithTeams?) {
        if(matchWithTeams != null) {
            match = matchWithTeams.match
        }
    }

    fun setTeamsAndPlayers(teamWithPlayers: List<TeamWithPlayers>?) {
        if(teamWithPlayers == null || teamWithPlayers.size < 2){
            return
        }
        homeTeam = teamWithPlayers[0]
        guestTeam = teamWithPlayers[1]

    }

    val homeTeamScore = MutableLiveData<Int>()
    val guestTeamScore = MutableLiveData<Int>()

    init {
        homeTeamScore.value = 0
        guestTeamScore.value = 0
    }

    fun calculateScore(liveActions: List<LiveAction>?) {
        if(liveActions == null){
            return
        }
        var homeScore = 0
        var guestScore = 0
        for(action in liveActions){
            if(action.teamId == homeTeam.team.teamId) {
                homeScore += getPointFromLiveAction(action)
            }else if(action.teamId == guestTeam.team.teamId){
                guestScore += getPointFromLiveAction(action)
            }
        }
        homeTeamScore.value = homeScore
        guestTeamScore.value = guestScore

    }


    private fun getPointFromLiveAction(liveAction: LiveAction): Int {
        if(liveAction.action is FootballActions){
            if((liveAction.action as FootballActions) == FootballActions.GOAL){
                return 1
            }
        }else if(liveAction.action is HandballActions) {
            val action = (liveAction.action as HandballActions)
            if(action == HandballActions.GOAL_6MTS || action == HandballActions.GOAL_7MTS || action == HandballActions.GOAL_9MTS){
                return 1
            }
        }else if(liveAction.action is BasketballActions) {
            if((liveAction.action as BasketballActions) == BasketballActions.SCORE_1_POINT){
                return 1
            }else if((liveAction.action as BasketballActions) == BasketballActions.SCORE_2_POINTS){
                return 2
            }else if((liveAction.action as BasketballActions) == BasketballActions.SCORE_3_POINTS){
                return 3
            }
        }
        return 0
    }
}