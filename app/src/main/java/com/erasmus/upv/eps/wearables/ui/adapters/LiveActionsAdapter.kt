package com.erasmus.upv.eps.wearables.ui.adapters

import android.bluetooth.BluetoothDevice
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.databinding.FragmentCurrentMatchBinding
import com.erasmus.upv.eps.wearables.databinding.ItemViewLiveActionsBinding
import com.erasmus.upv.eps.wearables.model.LiveAction
import timber.log.Timber

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
        holder.binding.apply {
            timeTv.text = currentLiveAction.time
            eventTv.text = currentLiveAction.action.toString()

            if(currentLiveAction.teamId == 0L){
                teamTv.visibility = View.INVISIBLE
            }else{
                teamTv.text = getTeamNameById.invoke(currentLiveAction.teamId)
                teamTv.backgroundTintList = ColorStateList.valueOf(getTeamColor.invoke(currentLiveAction.teamId))
            }

            if(currentLiveAction.playerId == 0L){
                playerTv.visibility = View.INVISIBLE
            }else{
                playerTv.backgroundTintList = ColorStateList.valueOf(getTeamColor.invoke(currentLiveAction.teamId))
                playerTv.text = getPlayerNameById.invoke(currentLiveAction.playerId)
            }

            deleteLiveAction.setOnClickListener {
                onDeleteClick.invoke(currentLiveAction)
            }
        }

    }

    override fun submitList(list: MutableList<LiveAction>?) {
        super.submitList(list?.let { ArrayList(it) })
    }


    inner class LiveActionsViewHolder(val binding: ItemViewLiveActionsBinding) : RecyclerView.ViewHolder(binding.root)

    class LiveActionsComparator: DiffUtil.ItemCallback<LiveAction>(){
        override fun areItemsTheSame(oldItem: LiveAction, newItem: LiveAction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LiveAction, newItem: LiveAction): Boolean {
            return oldItem == newItem
        }

    }

}