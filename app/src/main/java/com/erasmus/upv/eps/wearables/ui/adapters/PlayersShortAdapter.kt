package com.erasmus.upv.eps.wearables.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.databinding.ItemViewPlayerBinding
import com.erasmus.upv.eps.wearables.databinding.ItemViewPlayerShortBinding
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.ui.adapters.comparators.PlayersComparator

class PlayersShortAdapter(
        private val choosePlayer: (player: Player) -> Unit = {},
        private val deletePlayer: (player: Player) -> Unit = {}
                          ) : ListAdapter<Player, PlayersShortAdapter.PlayersShortViewHolder>(PlayersComparator()) {


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
        holder.binding.deletePlayerIv.setOnClickListener {
            deletePlayer.invoke(getItem(position))
        }
    }

    override fun submitList(list: List<Player>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    inner class PlayersShortViewHolder(val binding: ItemViewPlayerShortBinding) : RecyclerView.ViewHolder(binding.root)



}