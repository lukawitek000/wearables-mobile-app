package com.erasmus.upv.eps.wearables.ui.adapters

import android.bluetooth.BluetoothDevice
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.ItemViewScanResultBinding
import com.erasmus.upv.eps.wearables.model.Player

class ScanResultsAdapter(private val onClickScanResult: (BluetoothDevice) -> Unit)
    : ListAdapter<BluetoothDevice, ScanResultsAdapter.ScanResultsViewHolder>(
        ScanResultsComparator()
) {


    private val selectedDevices = emptyList<BluetoothDevice>().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanResultsViewHolder {
        return ScanResultsViewHolder(
                ItemViewScanResultBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ScanResultsViewHolder, position: Int) {

        holder.binding.deviceNameTextView.text = getItem(position).name ?: "Unknown Device"

        holder.binding.selectedDeviceCb.isChecked = selectedDevices.contains(getItem(position))

        holder.binding.selectedDeviceCb.setOnCheckedChangeListener { _, _ ->
            controlSelectedDevices(position)
        }

    }

    private fun controlSelectedDevices(position: Int) {
        if (!selectedDevices.contains(getItem(position))) {
            selectedDevices.add(getItem(position))
        } else {
            selectedDevices.remove(getItem(position))
        }
        onClickScanResult(getItem(position))
    }

    override fun submitList(list: MutableList<BluetoothDevice>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    inner class ScanResultsViewHolder(val binding: ItemViewScanResultBinding): RecyclerView.ViewHolder(binding.root) {

    }

    class ScanResultsComparator(): DiffUtil.ItemCallback<BluetoothDevice>(){
        override fun areItemsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
            return oldItem.address == newItem.address
        }

        override fun areContentsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
            return oldItem == newItem
        }

    }


}