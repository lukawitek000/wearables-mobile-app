package com.erasmus.upv.eps.wearables.model

import android.graphics.Color
import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Team (
    @PrimaryKey(autoGenerate = true)
    var teamId: Long,
    val name: String,
    //val players: List<Player>,
    val sport: String,
    val color: Int,
    val country: String,
    val city: String,
   // val logo: Uri?,
    val others: String
)