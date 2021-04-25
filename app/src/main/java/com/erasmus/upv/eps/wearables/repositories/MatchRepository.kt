package com.erasmus.upv.eps.wearables.repositories

import androidx.lifecycle.LiveData
import com.erasmus.upv.eps.wearables.db.dao.MatchDao
import com.erasmus.upv.eps.wearables.model.Match
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MatchRepository
    constructor(
    private val matchDao: MatchDao
) {

        suspend fun insertMatch(match: Match){
            withContext(Dispatchers.IO){
                matchDao.insertMatch(match)
            }
        }

    var allMatches = matchDao.getAllMatches()


    fun getMatchById(id: Long): LiveData<Match>{
        return matchDao.getMatchById(id)
    }

}