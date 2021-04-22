package com.erasmus.upv.eps.wearables.repositories

import com.erasmus.upv.eps.wearables.db.dao.TeamDao
import com.erasmus.upv.eps.wearables.model.Team
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TeamRepository (
    private val teamDao: TeamDao
        ) {

    suspend fun saveTeam(team: Team){
        withContext(Dispatchers.IO){
            teamDao.insertTeam(team)
        }
    }

    val allTeams = teamDao.getAllTeams()

}