package com.erasmus.upv.eps.wearables.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.databinding.ItemViewDevicesConfigurationBinding
import com.erasmus.upv.eps.wearables.model.BLEDevice
import com.erasmus.upv.eps.wearables.model.BLEDeviceWithGestures
import com.erasmus.upv.eps.wearables.model.Gesture
import com.erasmus.upv.eps.wearables.util.BLEConnectionManager

class DevicesConfigurationAdapter(private val devices: List<BLEDeviceWithGestures>,
                                  private val context: Context,
                                  private val onGestureClick: (device: BLEDevice, gesture: Gesture) -> Unit,
                                  private val reconnectDevice: (address: String) -> Unit
                                  ) : RecyclerView.Adapter<DevicesConfigurationAdapter.DevicesConfigurationViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevicesConfigurationViewHolder {
        return DevicesConfigurationViewHolder(
                ItemViewDevicesConfigurationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }


    override fun onBindViewHolder(holder: DevicesConfigurationViewHolder, position: Int) {
        val device = devices[position]
        holder.binding.deviceNameTv.text = device.bleDevice.name
        val gattsStatus = BLEConnectionManager.bluetoothGattsStatus.value ?: emptyMap<String, BLEConnectionManager.GattStatus>()
        if(!gattsStatus.containsKey(device.bleDevice.address) || gattsStatus[device.bleDevice.address] == BLEConnectionManager.GattStatus.CONNECTING){
            holder.binding.apply {
                connectingPb.visibility = View.VISIBLE
                deviceGestureRv.visibility = View.GONE
                failInfoL.visibility = View.GONE
            }
        }else if(gattsStatus[device.bleDevice.address] == BLEConnectionManager.GattStatus.DISCONNECTED) {
            holder.binding.apply {
                connectingPb.visibility = View.GONE
                deviceGestureRv.visibility = View.GONE
                failInfoL.visibility = View.VISIBLE

                reconnectBleDeviceBt.setOnClickListener {
                    reconnectDevice.invoke(device.bleDevice.address)
                }
            }
        }else{
            holder.binding.apply {
                connectingPb.visibility = View.GONE
                deviceGestureRv.visibility = View.VISIBLE
                failInfoL.visibility = View.GONE
            }
            val rv = holder.binding.deviceGestureRv
            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rv.adapter = GestureConfigurationAdapter(device.gestures) {
                onGestureClick.invoke(devices[position].bleDevice, it)
            }
        }
    }

    override fun getItemCount(): Int = devices.size



    inner class DevicesConfigurationViewHolder(val binding: ItemViewDevicesConfigurationBinding) : RecyclerView.ViewHolder(binding.root)

}