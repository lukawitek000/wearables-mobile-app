package com.erasmus.upv.eps.wearables.model

data class Player(
    val id: String,
    val name: String,
    val sport: String,
    val number: Int,
    val position: String,
    val otherInfo: String = "", // default value just for now, because of fake data
    val photo: String = "" // TODO add taking photos or getting photos from device
)