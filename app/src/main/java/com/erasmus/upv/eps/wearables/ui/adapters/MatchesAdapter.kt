package com.erasmus.upv.eps.wearables.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.databinding.ItemViewMatchBinding
import com.erasmus.upv.eps.wearables.model.Match
import java.text.SimpleDateFormat
import java.util.*

class MatchesAdapter(private val onClick: (match: Match) -> Unit) : ListAdapter<Match, MatchesAdapter.MatchesViewHolder>(
    MatchesComparator()
) {


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
        holder.binding.sportNameTv.text = getItem(position).sport
       // holder.binding.team1NameTv.text = matches[position].homeTeam.name
       // holder.binding.team2NameTv.text = matches[position].guestTeam.name
        val c = Calendar.getInstance()
        c.timeInMillis = getItem(position).date.time
        holder.binding.dayNumberTv.text = c.get(Calendar.DAY_OF_MONTH).toString()
        val monthDate = SimpleDateFormat("MMM")
        val monthName: String = monthDate.format(c.time)
        holder.binding.monthNameTv.text = monthName
        holder.binding.matchTimeTv.text = "${c.get(Calendar.HOUR_OF_DAY)}:${c.get(Calendar.MINUTE)}"
        holder.itemView.setOnClickListener { onClick.invoke(getItem(position)) }
    }

    inner class MatchesViewHolder(val binding: ItemViewMatchBinding): RecyclerView.ViewHolder(
        binding.root
    ){
    }

    class MatchesComparator: DiffUtil.ItemCallback<Match>() {
        override fun areItemsTheSame(oldItem: Match, newItem: Match): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Match, newItem: Match): Boolean {
            return oldItem.id == newItem.id
        }
    }


}