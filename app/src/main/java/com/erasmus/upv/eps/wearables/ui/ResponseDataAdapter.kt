package com.erasmus.upv.eps.wearables.ui

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.model.Response
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class ResponseDataAdapter(private val data: List<Response>): RecyclerView.Adapter<ResponseDataAdapter.ResponseDataViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResponseDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.response_data_view, parent, false)
        return ResponseDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResponseDataViewHolder, position: Int) {
        holder.timeStampTextView.text = convertLongToTime(data[position].timeStamp)

        //holder.timeStampTextView.text = data[position].timeStamp.toString()
        holder.receivedDataTextView.text = data[position].data.toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }


    class ResponseDataViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val timeStampTextView: TextView = view.findViewById<TextView>(R.id.time_stamp_textView)
        val receivedDataTextView: TextView = view.findViewById<TextView>(R.id.received_data_textView)


    }

    private fun convertLongToTime(time: Long): String {
        var formatter: String
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val date = Instant
                .ofEpochMilli(time)
                .atZone(ZoneId.systemDefault()) // change time zone if necessary
                .toLocalDateTime()

            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(date)
        }else{
            val date = Date(time)
            formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date)
        }


        return formatter
/*
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
        return format.format(date)*/
    }



}