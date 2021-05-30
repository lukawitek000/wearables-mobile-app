package com.erasmus.upv.eps.wearables.db.util

import androidx.room.TypeConverter
import com.erasmus.upv.eps.wearables.model.actions.Actions
import com.erasmus.upv.eps.wearables.model.actions.BasketballActions
import com.erasmus.upv.eps.wearables.model.actions.FootballActions
import com.erasmus.upv.eps.wearables.model.actions.HandballActions
import java.lang.Exception
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


    // TODO find better why of saving actions to database


    private val footballActionsPrefix = "FootballActions"
    private val basketballActionsPrefix = "BasketballActions"
    private val handballActionsPrefix = "HandballActions"

    @TypeConverter
    fun fromActionsToString(actions: Actions?): String {
        return when (actions) {
            is FootballActions -> {
                "$footballActionsPrefix${actions.name}"
            }
            is BasketballActions -> {
                "$basketballActionsPrefix${actions.name}"
            }
            is HandballActions -> {
                "$handballActionsPrefix${actions.name}"
            }
            else -> {
                ""
            }
        }
    }

    @TypeConverter
    fun fromStringToActions(text: String): Actions? {
        return when {
            text.startsWith(footballActionsPrefix) -> {
                enumValueOf<FootballActions>(text.subSequence(footballActionsPrefix.length, text.length).toString())
            }
            text.startsWith(basketballActionsPrefix) -> {
                enumValueOf<BasketballActions>(text.subSequence(basketballActionsPrefix.length, text.length).toString())
            }
            text.startsWith(handballActionsPrefix) -> {
                enumValueOf<HandballActions>(text.subSequence(handballActionsPrefix.length, text.length).toString())
            }
            else -> {
                null
            }
        }
    }


}