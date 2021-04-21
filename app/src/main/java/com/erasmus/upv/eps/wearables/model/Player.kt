package com.erasmus.upv.eps.wearables.model

data class Player(
    var id: String,
    var name: String,
    var sport: String,
    var number: Int,
    var position: String,
    var otherInfo: String = "", // default value just for now, because of fake data
    var photo: String = "" // TODO add taking photos or getting photos from device
)