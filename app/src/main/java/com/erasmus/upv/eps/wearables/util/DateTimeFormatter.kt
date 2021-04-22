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


}