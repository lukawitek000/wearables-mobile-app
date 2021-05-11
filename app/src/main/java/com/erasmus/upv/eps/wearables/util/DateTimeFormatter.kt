package com.erasmus.upv.eps.wearables.util

import java.text.SimpleDateFormat

object DateTimeFormatter {

    fun displayTime(value: Long): String {
        val timeFormat = SimpleDateFormat("HH:mm")
        return timeFormat.format(value)
    }

    fun displayDate(value: Long): String {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        return dateFormatter.format(value)
    }

    fun displayMinutesAndSeconds(value: Long): String{
        val valueInSeconds = value * 1000
        val timeFormat = SimpleDateFormat("mm:ss")
        return timeFormat.format(valueInSeconds)
    }


}