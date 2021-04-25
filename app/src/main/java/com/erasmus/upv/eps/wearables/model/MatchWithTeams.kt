package com.erasmus.upv.eps.wearables.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class MatchWithTeams(
    @Embedded val match: Match,
    @Relation(
        parentColumn = "matchId",
        entityColumn = "teamId",
        associateBy = Junction(MatchTeamCrossRef::class)
    )
    val teams: List<Team>
)