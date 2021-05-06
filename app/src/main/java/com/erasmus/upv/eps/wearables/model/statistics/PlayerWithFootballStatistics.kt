package com.erasmus.upv.eps.wearables.model.statistics

import androidx.room.Embedded
import androidx.room.Relation
import com.erasmus.upv.eps.wearables.model.Player

data class PlayerWithFootballStatistics(
        @Embedded
        val player: Player,
        @Relation(
                parentColumn = "playerId",
                entityColumn = "playerId"
        )
        val footballStatistics: List<FootballStatistics>
)