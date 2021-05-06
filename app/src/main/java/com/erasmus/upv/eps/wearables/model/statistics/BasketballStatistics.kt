package com.erasmus.upv.eps.wearables.model.statistics

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BasketballStatistics(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val matchId: Long = 0L,
        val teamId: Long = 0L,
        val playerId: Long = 0L,
        //val time: Date? = null,
        val score: Int,
        val score2Points: Int,
        val score3Points: Int,
        val score1Point: Int,
        val blocks: Int,
        val assists: Int
)