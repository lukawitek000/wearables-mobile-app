package com.erasmus.upv.eps.wearables.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.databinding.ItemViewPlayerBinding
import com.erasmus.upv.eps.wearables.databinding.ItemViewPlayerShortBinding
import com.erasmus.upv.eps.wearables.model.Player

class PlayersShortAdapter(
        private val choosePlayer: (player: Player) -> Unit = {}
                          ) : ListAdapter<Player, PlayersShortAdapter.PlayersShortViewHolder>(PlayersAdapter.PlayersComparator()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersShortViewHolder {
        return PlayersShortViewHolder(ItemViewPlayerShortBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        ))
    }

    override fun onBindViewHolder(holder: PlayersShortViewHolder, position: Int) {
        holder.binding.playerNameTv.text = getItem(position).name
        holder.binding.playerNumberTv.text = getItem(position).number.toString()
        holder.itemView.setOnClickListener {
            choosePlayer.invoke(getItem(position))
        }
    }

    inner class PlayersShortViewHolder(val binding: ItemViewPlayerShortBinding) : RecyclerView.ViewHolder(binding.root)



}