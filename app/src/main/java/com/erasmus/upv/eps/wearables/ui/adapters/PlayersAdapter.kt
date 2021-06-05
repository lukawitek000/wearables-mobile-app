package com.erasmus.upv.eps.wearables.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.databinding.ItemViewPlayerBinding
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.ui.adapters.comparators.PlayersComparator
import java.util.*

class PlayersAdapter(
    private val onClickPlayerItem: (player: Player) -> Unit): ListAdapter<Player, PlayersAdapter.PlayersViewHolder>(PlayersComparator()) {

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
        val player = getItem(position)
        holder.binding.playerNameAdapterTv.text = player.name
        holder.binding.playerNationalityAdapterTv.text = player.nationality.toUpperCase(Locale.ROOT)
        holder.itemView.setOnClickListener {
            onClickPlayerItem.invoke(player)
        }
    }

    inner class PlayersViewHolder(val binding: ItemViewPlayerBinding) : RecyclerView.ViewHolder(binding.root){}

}