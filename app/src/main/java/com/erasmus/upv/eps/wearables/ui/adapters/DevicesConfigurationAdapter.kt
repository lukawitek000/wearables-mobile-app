package com.erasmus.upv.eps.wearables.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.databinding.ItemViewDevicesConfigurationBinding
import com.erasmus.upv.eps.wearables.model.BLEDevice
import com.erasmus.upv.eps.wearables.model.Gesture

class DevicesConfigurationAdapter(private val devices: List<BLEDevice>,
                                  private val context: Context,
                                  private val onGestureClick: (device: BLEDevice, gesture: Gesture) -> Unit
                                  ) : RecyclerView.Adapter<DevicesConfigurationAdapter.DevicesConfigurationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevicesConfigurationViewHolder {
        return DevicesConfigurationViewHolder(
                ItemViewDevicesConfigurationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DevicesConfigurationViewHolder, position: Int) {
        holder.binding.deviceNameTv.text = devices[position].name
        val rv = holder.binding.deviceGestureRv
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        //rv.adapter = GestureConfigurationAdapter(devices[position].gestures){
        rv.adapter = GestureConfigurationAdapter(emptyList()){
                onGestureClick.invoke(devices[position], it)
            }


    }

    override fun getItemCount(): Int = devices.size



    inner class DevicesConfigurationViewHolder(val binding: ItemViewDevicesConfigurationBinding) : RecyclerView.ViewHolder(binding.root)

}