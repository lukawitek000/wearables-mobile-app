package com.erasmus.upv.eps.wearables.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.erasmus.upv.eps.wearables.model.actions.Actions

@Entity
data class LiveAction(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val matchId: Long,
        val teamId: Long,
        val playerId: Long,
        val time: String,
        val action: Actions?,
)