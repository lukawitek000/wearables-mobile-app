package com.erasmus.upv.eps.wearables.model

import android.graphics.Color
import android.net.Uri

data class Team (
    val id: String,
    val name: String,
    val players: List<Player>,
    val sport: String,
    val color: Color,
    val country: String,
    val city: String,
    val logo: Uri,
    val others: String
)