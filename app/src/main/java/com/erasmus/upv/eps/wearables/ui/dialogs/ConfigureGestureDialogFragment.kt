package com.erasmus.upv.eps.wearables.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.DialogFragmentConfigureGestureBinding
import com.erasmus.upv.eps.wearables.model.BLEDevice
import com.erasmus.upv.eps.wearables.model.Gesture
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ConfigureGestureDialogFragment(private val device: BLEDevice, private val gesture: Gesture) : BottomSheetDialogFragment() {


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

        binding.configGestureNameTv.text = gesture.name
        binding.doneConfigGestureBn.setOnClickListener {
            dismiss()
        }
        configDropDownInputs()

        return binding.root
    }

    private fun configDropDownInputs() {
        configSelectActionInput()
        configSelectTeamInput()
        configSelectPlayerInput()
    }

    private fun configSelectPlayerInput() {
        val players = listOf("Ana", "Luke", "Piotr")
        val adapter = ArrayAdapter(requireContext(), R.layout.input_list_item, players)
        (binding.selectPlayerForGestureTf.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun configSelectTeamInput() {
        val teams = listOf("Real Madrid", "Valencia FC")
        val adapter = ArrayAdapter(requireContext(), R.layout.input_list_item, teams)
        (binding.selectTeamForGestureTf.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun configSelectActionInput() {
        val actions = listOf("Goal", "Assist", "Foul")
        val adapter = ArrayAdapter(requireContext(), R.layout.input_list_item, actions)
        (binding.selectActionTf.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }


}