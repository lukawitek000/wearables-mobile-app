package com.erasmus.upv.eps.wearables.model.statistics

import androidx.room.Embedded
import androidx.room.Relation
import com.erasmus.upv.eps.wearables.model.Team

data class TeamWithFootballStatistics(
     @Embedded
     val team: Team,
     @Relation(
             parentColumn = "teamId",
             entityColumn = "teamId"
     )
     val footballStatistics: List<FootballStatistics>
)