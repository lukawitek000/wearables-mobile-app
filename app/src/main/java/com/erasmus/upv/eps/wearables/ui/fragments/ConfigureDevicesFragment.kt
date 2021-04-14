package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.erasmus.upv.eps.wearables.R


class ConfigureDevicesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_configure_devices, container, false)

        view.findViewById<Button>(R.id.configure_gesture_bt).setOnClickListener {
            ConfigureGestureFragment().show(childFragmentManager, ConfigureGestureFragment.TAG)
        }

        view.findViewById<Button>(R.id.done_bt).setOnClickListener {
            findNavController().navigate(R.id.action_configureDevicesFragment_to_currentMatchFragment)
        }


        return view
    }


}