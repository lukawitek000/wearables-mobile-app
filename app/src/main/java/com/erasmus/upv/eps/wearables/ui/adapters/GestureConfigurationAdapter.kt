package com.erasmus.upv.eps.wearables.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
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
        val currentGesture = gestures[position]
        holder.binding.gestureNameTv.text = currentGesture.name
        holder.itemView.setOnClickListener { onClick.invoke(currentGesture) }
        changeColorOfBackgroundForConfiguredGesture(currentGesture, holder.binding.gestureCardView)
    }

    private fun changeColorOfBackgroundForConfiguredGesture(
        currentGesture: Gesture,
        cardView: CardView
    ) {
        if(currentGesture.action != null ){
            if(currentGesture.assignTeamId != 0L){
                if(currentGesture.assignPlayerId != 0L){
                    cardView.setCardBackgroundColor(Color.GREEN)
                }else{
                    cardView.setCardBackgroundColor(Color.rgb(255, 165, 0))
                }
            }else{
                cardView.setCardBackgroundColor(Color.YELLOW)
            }
        }else{
            cardView.setCardBackgroundColor(Color.RED)
        }
        cardView.radius = 12.0f
    }

    override fun getItemCount(): Int = gestures.size


    inner class GestureConfigurationViewHolder(val binding: ItemViewGestureConfigurationBinding): RecyclerView.ViewHolder(binding.root)


}