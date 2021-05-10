package com.erasmus.upv.eps.wearables.repositories

import androidx.lifecycle.LiveData
import com.erasmus.upv.eps.wearables.db.dao.StatisticsDao
import com.erasmus.upv.eps.wearables.model.LiveAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class StatisticsRepository
constructor(
        private val statisticsDao: StatisticsDao
) {
    suspend fun insertLastLiveAction(lastLiveAction: LiveAction) {
        withContext(Dispatchers.IO){
            statisticsDao.insertLiveAction(lastLiveAction)
        }
    }

    fun getLiveActionsForTheMatch(matchId: Long): Flow<List<LiveAction>> {
        return statisticsDao.getLiveActionsForTheMatch(matchId)
    }

    suspend fun deleteLiveActionById(id: Long) {
        withContext(Dispatchers.IO){
            statisticsDao.deleteLiveActionById(id)
        }
    }


}