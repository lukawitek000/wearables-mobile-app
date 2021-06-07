package com.erasmus.upv.eps.wearables.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.databinding.ItemViewMatchBinding
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.model.MatchWithTeams
import com.erasmus.upv.eps.wearables.util.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

class MatchesAdapter(private val onClick: (match: Match) -> Unit,
                    private val onClickShowMatchInfo: (match: Match) -> Unit) : ListAdapter<MatchWithTeams, MatchesAdapter.MatchesViewHolder>(
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
        val matchWithTeams = getItem(position)
        val match = matchWithTeams.match
        if(matchWithTeams.teams.size >= 2) {
            val homeTeam = matchWithTeams.teams[0]
            val guestTeam = matchWithTeams.teams[1]
            holder.binding.team1NameTv.text = homeTeam.name
            holder.binding.team2NameTv.text = guestTeam.name
        }
        holder.binding.sportNameTv.text = match.sport
        val c = Calendar.getInstance()
        c.timeInMillis = match.date.time
        holder.binding.dayNumberTv.text = c.get(Calendar.DAY_OF_MONTH).toString()
        val monthDate = SimpleDateFormat("MMM")
        val monthName: String = monthDate.format(c.time)
        holder.binding.monthNameTv.text = monthName
        holder.binding.matchTimeTv.text = DateTimeFormatter.displayTime(c.timeInMillis)
        holder.itemView.setOnClickListener { onClick.invoke(match) }
        holder.binding.matchInfoIv.setOnClickListener { onClickShowMatchInfo.invoke(match) }
    }

    inner class MatchesViewHolder(val binding: ItemViewMatchBinding): RecyclerView.ViewHolder(
        binding.root
    ){
    }

    class MatchesComparator: DiffUtil.ItemCallback<MatchWithTeams>() {
        override fun areItemsTheSame(oldItem: MatchWithTeams, newItem: MatchWithTeams): Boolean {
            return oldItem.match.matchId == newItem.match.matchId
        }

        override fun areContentsTheSame(oldItem: MatchWithTeams, newItem: MatchWithTeams): Boolean {
            return oldItem == newItem
        }

    }


}