package com.erasmus.upv.eps.wearables.db.util

import androidx.room.TypeConverter
import com.erasmus.upv.eps.wearables.model.actions.Actions
import com.erasmus.upv.eps.wearables.model.actions.FootballActions
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

    @TypeConverter
    fun fromFootballActionsToString(actions: FootballActions): String = actions.name

    @TypeConverter
    fun fromStringToFootballActions(text: String): FootballActions = enumValueOf(text)


    @TypeConverter
    fun fromActionsToString(actions: Actions): String {
        if(actions is FootballActions){
            return "FootballActions ${actions.name}"
        }else if {
            return ""
        }
    }

}