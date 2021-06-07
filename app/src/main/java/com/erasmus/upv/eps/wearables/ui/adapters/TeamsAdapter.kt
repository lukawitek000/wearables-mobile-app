package com.erasmus.upv.eps.wearables.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.ItemViewTeamBinding
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.util.Sports

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
        val sportIv = holder.binding.teamSportIv
        when(team.sport){
            Sports.FOOTBALL -> sportIv.setImageResource(R.drawable.football_unselected)
            Sports.BASKETBALL -> sportIv.setImageResource(R.drawable.basketball_unselected)
            else -> sportIv.setImageResource(R.drawable.handball_unselected)
        }
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