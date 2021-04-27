package com.erasmus.upv.eps.wearables.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.databinding.ItemViewPlayerBinding
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.ui.adapters.comparators.PlayersComparator

class PlayersAdapter(
    private val onClickPlayerItem: (player: Player) -> Unit,
    private val onClickDeletePlayer: (player: Player) -> Unit): ListAdapter<Player, PlayersAdapter.PlayersViewHolder>(PlayersComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersViewHolder {
        return PlayersViewHolder(
                ItemViewPlayerBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {
        holder.binding.playerNameAdapterTv.text = getItem(position).name
        holder.itemView.setOnClickListener {
            onClickPlayerItem.invoke(getItem(position))
        }
        holder.binding.deletePlayerForeverIv.setOnClickListener {
            onClickDeletePlayer.invoke(getItem(position))
        }
    }

    inner class PlayersViewHolder(val binding: ItemViewPlayerBinding) : RecyclerView.ViewHolder(binding.root){}

}