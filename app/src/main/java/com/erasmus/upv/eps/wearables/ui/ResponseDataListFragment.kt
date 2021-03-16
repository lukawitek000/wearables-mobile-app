package com.erasmus.upv.eps.wearables.ui


import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.service.BLEConnectionForegroundService
import com.erasmus.upv.eps.wearables.util.BLEConnectionManager
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

        //viewModel.responseData = BLEConnectionForegroundService.receiveData

        val recyclerView = view.findViewById<RecyclerView>(R.id.received_data_recyclerView)
       // receivedDataAdapter = ResponseDataAdapter(viewModel.responseData.value!!)
        //BLEConnectionForegroundService.initValues()
        receivedDataAdapter = ResponseDataAdapter(BLEConnectionManager.responseList.value!!)
        recyclerView.adapter = receivedDataAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        view.findViewById<Button>(R.id.stop_receiving_data_button).setOnClickListener {
            sendCommandToBLEConnectionService("STOP")
            findNavController().navigateUp()
        }




        //BLEConnectionManager.responseList = viewModel.responseData

        //BLEConnectionManager.responseList = BLEConnectionForegroundService.receiveData

       /* viewModel.responseData.observe(viewLifecycleOwner){
            Log.i("Scan", "onCreateView: data changed $it")
            receivedDataAdapter.notifyDataSetChanged()
        }
        */

        BLEConnectionForegroundService.receiveData.observe(viewLifecycleOwner){
            Log.i("Scan", "onCreateView: data changed $it")
            receivedDataAdapter.notifyDataSetChanged()
        }
        sendCommandToBLEConnectionService("START")

        /*
        Intent(requireContext(), BLEConnectionForegroundService::class.java).also { intent ->
            requireActivity().startService(intent)
        }*/



        return view
    }




    private fun sendCommandToBLEConnectionService(command: String){
        Intent(requireContext(), BLEConnectionForegroundService::class.java).apply {
            this.action = command
        }.also { intent ->
            requireActivity().startService(intent)
        }
    }






}