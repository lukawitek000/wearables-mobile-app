package com.erasmus.upv.eps.wearables.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Match(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    //val guestTeam: Team,
    //val homeTeam: Team,
    val date: String,
    val time: String,
    val location: String,
    val sport: String,
    val league: String,
    val otherDetails: String,
)
