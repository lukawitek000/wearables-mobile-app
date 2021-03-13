package com.erasmus.upv.eps.wearables.ui


import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.MainActivity
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.util.BLEConnectionManager
import java.text.SimpleDateFormat
import java.util.*

class ResponseDataListFragment : Fragment() {

    private lateinit var viewModel: ResponseDataListViewModel

    private lateinit var receivedDataAdapter: ResponseDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.response_data_list_fragment, container, false)
        viewModel = ViewModelProvider(this).get(ResponseDataListViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.received_data_recyclerView)
        receivedDataAdapter = ResponseDataAdapter(viewModel.responseData.value!!)
        recyclerView.adapter = receivedDataAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        view.findViewById<Button>(R.id.add_random_data_button).setOnClickListener {
            viewModel.addResponse()
            receivedDataAdapter.notifyDataSetChanged()
        }

        BLEConnectionManager.responseList = viewModel.responseData

        viewModel.responseData.observe(viewLifecycleOwner){
            Log.i("Scan", "onCreateView: data changed $it")
            receivedDataAdapter.notifyDataSetChanged()
        }


        return view
    }







}