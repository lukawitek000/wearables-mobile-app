package com.erasmus.upv.eps.wearables.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.databinding.ItemViewPlayerBinding
import com.erasmus.upv.eps.wearables.model.Player

class PlayersAdapter(
    private val onClick: (player: Player) -> Unit,
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
            onClick.invoke(getItem(position))
        }
        holder.binding.deletePlayerForeverIv.setOnClickListener {
            onClickDeletePlayer.invoke(getItem(position))
        }
    }



    inner class PlayersViewHolder(val binding: ItemViewPlayerBinding) : RecyclerView.ViewHolder(binding.root){}


    class PlayersComparator : DiffUtil.ItemCallback<Player>(){
        override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem.playerId == newItem.playerId
        }

    }
}