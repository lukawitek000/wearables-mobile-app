package com.erasmus.upv.eps.wearables.repositories

import androidx.lifecycle.LiveData
import com.erasmus.upv.eps.wearables.db.dao.PlayerDao
import com.erasmus.upv.eps.wearables.db.dao.TeamDao
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.model.TeamWithPlayers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TeamRepository (
    private val teamDao: TeamDao,
    private val playerDao: PlayerDao
        ) {

    suspend fun saveTeam(team: Team): Long{
        return withContext(Dispatchers.IO){
            teamDao.insertTeam(team)
        }
    }

    fun getTeamWithPlayers(id: Long): LiveData<TeamWithPlayers> {
        return teamDao.getTeamWithPlayers(id)
    }

    suspend fun deleteTeamById(teamId: Long) {
        withContext(Dispatchers.IO) {
            teamDao.deleteTeamById(teamId)
            playerDao.updatePlayersTeamIdFromTeam(teamId)
        }
    }

    suspend fun updateTeam(team: Team) {
        withContext(Dispatchers.IO){
            teamDao.updateTeam(team)
        }
    }

    val allTeams = teamDao.getAllTeams()

}