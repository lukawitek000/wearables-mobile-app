package com.erasmus.upv.eps.wearables.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.erasmus.upv.eps.wearables.R


class ScanningBluetoothFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scanning_bluetooth, container, false)
        view.findViewById<Button>(R.id.go_to_response_list_button).setOnClickListener {
            findNavController().navigate(R.id.action_scanningBluetoothFragment_to_responseDataListFragment)
        }
        return view
    }


}