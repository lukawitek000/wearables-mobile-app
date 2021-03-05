package com.erasmus.upv.eps.wearables.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.model.Response

class ResponseDataAdapter(private val data: List<Response>): RecyclerView.Adapter<ResponseDataAdapter.ResponseDataViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResponseDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.response_data_view, parent, false)
        return ResponseDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResponseDataViewHolder, position: Int) {
        holder.timeStampTextView.text = data[position].timeStamp.toString()
        holder.receivedDataTextView.text = data[position].data.toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }


    class ResponseDataViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val timeStampTextView: TextView = view.findViewById<TextView>(R.id.time_stamp_textView)
        val receivedDataTextView: TextView = view.findViewById<TextView>(R.id.received_data_textView)


    }



}