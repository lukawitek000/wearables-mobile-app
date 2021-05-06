package com.erasmus.upv.eps.wearables.model.statistics

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class FootballStatistics (
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val matchId: Long = 0L,
        val teamId: Long = 0L,
        val playerId: Long = 0L,
        //val time: Date? = null,
        val goals: Int,
        val assists: Int,
        val fouls: Int,
        val offsides: Int
)

// TODO save matchId to every statistics
// TODO save teamId if it is team specific statistic
// TODO save playerId if it is player specific statistic