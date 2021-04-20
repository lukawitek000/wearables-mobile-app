package com.erasmus.upv.eps.wearables.model

data class Device(
        val id: Long,
        val name: String,
        val gestures: List<Gesture>
)