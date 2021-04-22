package com.erasmus.upv.eps.wearables.repositories

import com.erasmus.upv.eps.wearables.db.dao.MatchDao
import com.erasmus.upv.eps.wearables.model.Match
import kotlinx.coroutines.Dispatchers
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



}