package com.erasmus.upv.eps.wearables.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.databinding.FragmentCurrentMatchBinding
import com.erasmus.upv.eps.wearables.databinding.ItemViewLiveActionsBinding
import com.erasmus.upv.eps.wearables.model.LiveAction

class LiveActionsAdapter(private val actions: List<LiveAction>) : RecyclerView.Adapter<LiveActionsAdapter.LiveActionsViewHolder>() {


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
        holder.binding.timeTv.text = actions[position].time
        holder.binding.eventTv.text = actions[position].event
        holder.binding.detailsTv.text = actions[position].details
    }

    override fun getItemCount(): Int = actions.size


    inner class LiveActionsViewHolder(val binding: ItemViewLiveActionsBinding) : RecyclerView.ViewHolder(binding.root)

}