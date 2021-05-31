package com.erasmus.upv.eps.wearables.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.erasmus.upv.eps.wearables.model.actions.Actions


@Entity
data class Gesture (
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val name: String,
        val receivedData: Int,
        var action: Actions?, // meaning, assign statistic to gesture
        var assignTeamId: Long,
        var assignPlayerId: Long,
        val bleDeviceId: String,
        var shouldAskAboutTeam: Boolean,
        var shouldAskAboutPlayer: Boolean
        )