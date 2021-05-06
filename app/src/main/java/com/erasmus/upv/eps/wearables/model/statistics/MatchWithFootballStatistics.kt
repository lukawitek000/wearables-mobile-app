package com.erasmus.upv.eps.wearables.model.statistics

import androidx.room.Embedded
import androidx.room.Relation
import com.erasmus.upv.eps.wearables.model.Match

data class MatchWithFootballStatistics(
        @Embedded
        val match: Match,
        @Relation(
                parentColumn = "matchId",
                entityColumn = "matchId"
        )
        val footballStatistics: FootballStatistics
)