package com.erasmus.upv.eps.wearables.model

import androidx.room.Entity


@Entity(primaryKeys = ["matchId", "teamId"])
data class MatchTeamCrossRef(
    val matchId: Long,
    val teamId: Long
)