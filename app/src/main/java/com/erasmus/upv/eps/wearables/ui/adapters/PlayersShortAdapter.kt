package com.erasmus.upv.eps.wearables.ui.adapters

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.ItemViewPlayerBinding
import com.erasmus.upv.eps.wearables.databinding.ItemViewPlayerShortBinding
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.ui.adapters.comparators.PlayersComparator

class PlayersShortAdapter(
    private val choosePlayer: (player: Player) -> Unit = {},
    private val deletePlayer: (player: Player) -> Unit = {},
    private val context: Context,
    private val isDeletable: Boolean = true
                          ) : ListAdapter<Player, PlayersShortAdapter.PlayersShortViewHolder>(PlayersComparator()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersShortViewHolder {
        return PlayersShortViewHolder(ItemViewPlayerShortBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        ))
    }

    override fun onBindViewHolder(holder: PlayersShortViewHolder, position: Int) {
        val player = getItem(position)
        holder.binding.playerNameTv.text = item.name
        holder.binding.playerNumberTv.text = item.number.toString()
        holder.itemView.setOnClickListener {
            choosePlayer.invoke(item)
        }
        if(isDeletable) {
            holder.binding.deletePlayerIv.setOnClickListener {
                deletePlayer.invoke(item)
            }
        }else{
            holder.binding.deletePlayerIv.visibility = View.GONE
            holder.binding.root.backgroundTintList = ContextCompat.getColorStateList(context, R.color.blue_ish)
            holder.binding.playerNameTv.visibility = View.GONE
        }
    }

    override fun submitList(list: List<Player>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    inner class PlayersShortViewHolder(val binding: ItemViewPlayerShortBinding) : RecyclerView.ViewHolder(binding.root)



}