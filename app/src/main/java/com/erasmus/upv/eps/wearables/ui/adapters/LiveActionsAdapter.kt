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

class LiveActionsAdapter() : ListAdapter<LiveAction, LiveActionsAdapter.LiveActionsViewHolder>(LiveActionsComparator()){


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
        holder.binding.timeTv.text = getItem(position).time
        holder.binding.eventTv.text = getItem(position).action.toString()
        holder.binding.detailsTv.text = "team = ${getItem(position).teamId} player = ${getItem(position).playerId}"
    }

    override fun submitList(list: MutableList<LiveAction>?) {
        super.submitList(list?.let { ArrayList(it) })
    }


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