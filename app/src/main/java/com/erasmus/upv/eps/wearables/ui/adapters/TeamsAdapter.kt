package com.erasmus.upv.eps.wearables.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.databinding.ItemViewTeamBinding
import com.erasmus.upv.eps.wearables.model.Team

class TeamsAdapter(private val onClickTeamItem: (team: Team) -> Unit
                   ) : ListAdapter<Team, TeamsAdapter.TeamsViewHolder>(TeamsComparator()) {

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
        val team = getItem(position)
        holder.binding.teamNameAdapterTv.text = team.name
        holder.binding.teamCityAdapterTv.text = team.city
        holder.itemView.setOnClickListener { onClickTeamItem.invoke(team) }
    }

    inner class TeamsViewHolder(val binding: ItemViewTeamBinding) : RecyclerView.ViewHolder(binding.root)


    class TeamsComparator : DiffUtil.ItemCallback<Team>(){
        override fun areItemsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem.teamId == newItem.teamId
        }
    }

}