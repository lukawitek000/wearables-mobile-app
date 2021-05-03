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
        val action: Actions, // meaning, assign statistic to gesture
        val assignTeamId: Long,
        val assignPlayerId: Long,
        val bleDeviceId: String
        )