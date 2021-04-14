package com.erasmus.upv.eps.wearables.ui.adapters

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.R

class ScanResultsAdapter(private val scanResults: MutableList<BluetoothDevice>,
                         private val onClickScanResult: (BluetoothDevice) -> Unit)
    : RecyclerView.Adapter<ScanResultsAdapter.ScanResultsViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanResultsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_scan_result, parent, false)
        return ScanResultsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScanResultsViewHolder, position: Int) {

        holder.deviceNameTextView.text = scanResults[position].name ?: "No name"
        holder.deviceAddressTextView.text = scanResults[position].address

        holder.itemView.setOnClickListener {
            onClickScanResult(scanResults[position])
        }

    }

    override fun getItemCount(): Int = scanResults.size



    inner class ScanResultsViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val deviceNameTextView: TextView = view.findViewById(R.id.device_name_textView)
        val deviceAddressTextView: TextView = view.findViewById(R.id.device_address_textView)

    }


}