package com.erasmus.upv.eps.wearables.model

import androidx.room.Embedded
import androidx.room.Relation

data class TeamWithPlayers(
        @Embedded
        val team: Team,
        @Relation(
                parentColumn = "teamId",
                entityColumn = "teamOfPlayerId"
        )
        val players: List<Player>
)