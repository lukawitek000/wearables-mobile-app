package com.erasmus.upv.eps.wearables.model

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.*

@Entity
data class Match(
    @PrimaryKey(autoGenerate = true)
    var matchId: Long,
    var date: Date,
    var location: String,
    var city: String,
    var sport: String,
    var league: String,
    var otherDetails: String,
    var homeTeamColor: Int,
    var guestTeamColor: Int,
    var matchParts: Int,
    var matchPartDuration: Long
){
    constructor(): this(
            matchId = 0L,
            date = Date(),
            location = "",
            city = "",
            sport = "",
            league = "",
            otherDetails = "",
            homeTeamColor = Color.RED,
            guestTeamColor = Color.GREEN,
            matchParts = 0,
            matchPartDuration = 0L
    )
}
