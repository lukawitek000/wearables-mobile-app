package com.erasmus.upv.eps.wearables.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.*

@Entity
data class Match(
    @PrimaryKey(autoGenerate = true)
    var matchId: Long,
    //val guestTeam: Team,
    //val homeTeam: Team,
    var date: Date,
    val location: String,
    val sport: String,
    val league: String,
    val otherDetails: String,
)
