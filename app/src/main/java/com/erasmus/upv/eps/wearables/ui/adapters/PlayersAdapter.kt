package com.erasmus.upv.eps.wearables.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.databinding.ItemViewPlayerBinding
import com.erasmus.upv.eps.wearables.model.Player

class PlayersAdapter(private val players: List<Player>) : RecyclerView.Adapter<PlayersAdapter.PlayersViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersViewHolder {
        return PlayersViewHolder(ItemViewPlayerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        ))
    }

    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {
        holder.binding.playerNameTv.text = players[position].name
        holder.binding.playerNumberTv.text = players[position].number.toString()
    }

    override fun getItemCount(): Int = players.size



    inner class PlayersViewHolder(val binding: ItemViewPlayerBinding) : RecyclerView.ViewHolder(binding.root)



}