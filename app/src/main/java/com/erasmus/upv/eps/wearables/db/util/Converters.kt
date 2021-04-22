package com.erasmus.upv.eps.wearables.db.util

import androidx.room.TypeConverter
import java.util.*


class Converters {

    @TypeConverter
    fun fromTimeStamp(value: Long): Date{
        return Date(value)
    }

    @TypeConverter
    fun fromDateToTimeStamp(value: Date): Long {
        return value.time
    }


}