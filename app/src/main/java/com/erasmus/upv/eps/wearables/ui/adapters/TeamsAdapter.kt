package com.erasmus.upv.eps.wearables.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.databinding.ItemViewTeamBinding
import com.erasmus.upv.eps.wearables.model.Team

class TeamsAdapter(private val teams: List<Team>) : RecyclerView.Adapter<TeamsAdapter.TeamsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamsViewHolder {
        return TeamsViewHolder(
                ItemViewTeamBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: TeamsViewHolder, position: Int) {
        holder.binding.teamNameAdapterTv.text = teams[position].name
        holder.binding.teamCityAdapterTv.text = teams[position].city
    }

    override fun getItemCount(): Int = teams.size




    inner class TeamsViewHolder(val binding: ItemViewTeamBinding) : RecyclerView.ViewHolder(binding.root)

}