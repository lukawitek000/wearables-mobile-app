package com.erasmus.upv.eps.wearables.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Player(
    @PrimaryKey
    var id: Long,
    var name: String,
    var sport: String,
    var number: Int,
    var position: String,
    var otherInfo: String = "", // default value just for now, because of fake data
    var photo: String = "" // TODO add taking photos or getting photos from device
)