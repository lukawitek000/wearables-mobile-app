package com.erasmus.upv.eps.wearables.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.DialogFragmentConfigureGestureBinding
import com.erasmus.upv.eps.wearables.model.Device
import com.erasmus.upv.eps.wearables.model.Gesture
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ConfigureGestureDialogFragment(private val device: Device, private val gesture: Gesture) : BottomSheetDialogFragment() {


    companion object {
        const val TAG = "ConfigureGestureDialogFragment"
    }

    private lateinit var binding: DialogFragmentConfigureGestureBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentConfigureGestureBinding.inflate(
                inflater, container, false
        )

        binding.confGestureDeviceNameTv.text = device.name
        binding.gestureConfigGestureNameTv.text = gesture.name

        return binding.root
    }


}