package com.erasmus.upv.eps.wearables.model

import androidx.room.Entity
import androidx.room.PrimaryKey


//@Entity(primaryKeys = ["matchId", "teamId"])
@Entity
data class MatchTeamCrossRef(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0L,
        val matchId: Long,
        val teamId: Long
)