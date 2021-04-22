package com.erasmus.upv.eps.wearables.model

import java.time.LocalDate

data class Match(
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
