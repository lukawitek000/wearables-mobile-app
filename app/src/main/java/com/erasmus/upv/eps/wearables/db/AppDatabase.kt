package com.erasmus.upv.eps.wearables.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.erasmus.upv.eps.wearables.db.dao.MatchDao
import com.erasmus.upv.eps.wearables.db.dao.PlayerDao
import com.erasmus.upv.eps.wearables.db.dao.TeamDao
import com.erasmus.upv.eps.wearables.db.util.Converters
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.model.MatchTeamCrossRef
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.model.Team


@Database(entities = [Player::class, Team::class, Match::class, MatchTeamCrossRef::class], version = 9, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao
    abstract fun teamDao(): TeamDao
    abstract fun matchDao(): MatchDao
}