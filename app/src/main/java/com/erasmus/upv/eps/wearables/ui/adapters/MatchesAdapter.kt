package com.erasmus.upv.eps.wearables.ui.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.databinding.ItemViewMatchBinding
import com.erasmus.upv.eps.wearables.model.Match

class MatchesAdapter(private val matches: List<Match>, private val onClick: (match: Match) -> Unit) : RecyclerView.Adapter<MatchesAdapter.MatchesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesViewHolder {
        return MatchesViewHolder(
            ItemViewMatchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {
        holder.binding.sportNameTv.text = matches[position].sport
        holder.binding.team1NameTv.text = matches[position].homeTeam.name
        holder.binding.team2NameTv.text = matches[position].guestTeam.name
        holder.binding.dayNumberTv.text = matches[position].date.dayOfMonth.toString()
        holder.binding.monthNameTv.text = matches[position].date.month.name

        holder.itemView.setOnClickListener { onClick.invoke(matches[position]) }
    }

    override fun getItemCount(): Int = matches.size


    inner class MatchesViewHolder(val binding: ItemViewMatchBinding): RecyclerView.ViewHolder(binding.root){
    }


}