package com.erasmus.upv.eps.wearables.ui.adapters.comparators

import androidx.recyclerview.widget.DiffUtil
import com.erasmus.upv.eps.wearables.model.Player


class PlayersComparator : DiffUtil.ItemCallback<Player>(){
    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem.playerId == newItem.playerId
    }

}