package com.erasmus.upv.eps.wearables.model.statistics

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HandballStatistics(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val matchId: Long = 0L,
        val teamId: Long = 0L,
        val playerId: Long = 0L,
        //val time: Date? = null,
        val goals: Int,
        val foul: Int,
        val insidePenaltyBoxFaults: Int
)