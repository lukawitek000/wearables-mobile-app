package com.erasmus.upv.eps.wearables.ui.adapters

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.databinding.FragmentCurrentMatchBinding
import com.erasmus.upv.eps.wearables.databinding.ItemViewLiveActionsBinding
import com.erasmus.upv.eps.wearables.model.LiveAction

class LiveActionsAdapter(
    private val onDeleteClick: (liveAction: LiveAction) -> Unit,
    private val getTeamColor: (teamId: Long) -> Int,
    private val getPlayerNameById: (playerId: Long) -> String?,
    private val getTeamNameById: (teamId: Long) -> String?
)
    : ListAdapter<LiveAction, LiveActionsAdapter.LiveActionsViewHolder>(LiveActionsComparator()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveActionsViewHolder {
        return LiveActionsViewHolder(
                ItemViewLiveActionsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: LiveActionsViewHolder, position: Int) {
        val currentLiveAction = getItem(position)
        holder.binding.timeTv.text = currentLiveAction.time
        holder.binding.eventTv.text = currentLiveAction.action.toString()
        holder.binding.teamTv.text = getTeamNameById.invoke(currentLiveAction.teamId)
        holder.binding.teamTv.setBackgroundColor(getTeamColor.invoke(currentLiveAction.teamId))
        holder.binding.playerTv.text = getPlayerNameById.invoke(currentLiveAction.playerId)

        holder.binding.deleteLiveAction.setOnClickListener { onDeleteClick.invoke(getItem(position)) }

    }

//    override fun submitList(list: MutableList<LiveAction>?) {
//        super.submitList(list?.let { ArrayList(it) })
//    }


    inner class LiveActionsViewHolder(val binding: ItemViewLiveActionsBinding) : RecyclerView.ViewHolder(binding.root)

    class LiveActionsComparator: DiffUtil.ItemCallback<LiveAction>(){
        override fun areItemsTheSame(oldItem: LiveAction, newItem: LiveAction): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(oldItem: LiveAction, newItem: LiveAction): Boolean {
            return oldItem == newItem
        }

    }

}