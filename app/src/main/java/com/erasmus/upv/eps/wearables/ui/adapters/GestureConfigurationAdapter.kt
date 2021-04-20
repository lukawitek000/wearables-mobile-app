package com.erasmus.upv.eps.wearables.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.databinding.ItemViewGestureConfigurationBinding
import com.erasmus.upv.eps.wearables.model.Gesture

class GestureConfigurationAdapter(private val gestures: List<Gesture>,
                                  private val onClick: (gesture: Gesture) -> Unit) : RecyclerView.Adapter<GestureConfigurationAdapter.GestureConfigurationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GestureConfigurationViewHolder {
        return GestureConfigurationViewHolder(
                ItemViewGestureConfigurationBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: GestureConfigurationViewHolder, position: Int) {
        holder.binding.gestureNameTv.text = gestures[position].name
        holder.itemView.setOnClickListener { onClick.invoke(gestures[position]) }
    }

    override fun getItemCount(): Int = gestures.size


    inner class GestureConfigurationViewHolder(val binding: ItemViewGestureConfigurationBinding): RecyclerView.ViewHolder(binding.root)


}