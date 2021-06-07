package com.erasmus.upv.eps.wearables.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.databinding.ItemViewMatchBinding
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.util.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

class MatchesAdapter(private val onClick: (match: Match) -> Unit,
                    private val onClickShowMatchInfo: (match: Match) -> Unit) : ListAdapter<Match, MatchesAdapter.MatchesViewHolder>(
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
        val match = getItem(position)
        holder.binding.sportNameTv.text = match.sport
//        holder.binding.team1NameTv.text = match.
//        holder.binding.team2NameTv.text = match.guestTeam.name
        val c = Calendar.getInstance()
        c.timeInMillis = getItem(position).date.time
        holder.binding.dayNumberTv.text = c.get(Calendar.DAY_OF_MONTH).toString()
        val monthDate = SimpleDateFormat("MMM")
        val monthName: String = monthDate.format(c.time)
        holder.binding.monthNameTv.text = monthName
        holder.binding.matchTimeTv.text = DateTimeFormatter.displayTime(c.timeInMillis)
        holder.itemView.setOnClickListener { onClick.invoke(getItem(position)) }
        holder.binding.matchInfoIv.setOnClickListener { onClickShowMatchInfo.invoke(getItem(position)) }
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
            return oldItem.matchId == newItem.matchId
        }
    }


}