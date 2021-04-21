package com.erasmus.upv.eps.wearables.repositories

import android.content.Context
import com.erasmus.upv.eps.wearables.db.AppDatabase
import com.erasmus.upv.eps.wearables.model.Player

class PlayerRepository(private val context: Context) {

    private val playerDao = AppDatabase.createAppDatabase(context).playerDao()


    fun savePlayer(player: Player){
        playerDao.insertPlayer(player)
    }


}