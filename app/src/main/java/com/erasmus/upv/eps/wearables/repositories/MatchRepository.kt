package com.erasmus.upv.eps.wearables.repositories

import androidx.lifecycle.LiveData
import com.erasmus.upv.eps.wearables.db.dao.MatchDao
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.model.MatchTeamCrossRef
import com.erasmus.upv.eps.wearables.model.MatchWithTeams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MatchRepository
    constructor(
    private val matchDao: MatchDao
) {

    suspend fun insertMatch(match: Match) : Long{
        return withContext(Dispatchers.IO){
            matchDao.insertMatch(match)
        }
    }

    var allMatches = matchDao.getAllMatches()


    fun getMatchById(id: Long): LiveData<Match>{
        return matchDao.getMatchById(id)
    }

    suspend fun insertMatchTeamCrossRef(matchId: Long, teamId: Long){
        withContext(Dispatchers.IO){
            matchDao.insertMatchTeamCrossRef(
                MatchTeamCrossRef(matchId, teamId)
            )
        }
    }

    fun getMatchAndTeamsById(id: Long): LiveData<MatchWithTeams>{
        return matchDao.getMatchWithTeams(id)
    }

    suspend fun deleteMatchById(matchId: Long) {
        withContext(Dispatchers.IO){
            matchDao.deleteMatchById(matchId)
        }
    }

    suspend fun deleteMatchTeamCrossRefByMatchId(matchId: Long) {
        withContext(Dispatchers.IO){
            matchDao.deleteMatchTeamCrossRefByMatchId(matchId)
        }
    }


}