package com.erasmus.upv.eps.wearables.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.navGraphViewModels
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.DialogFragmentConfigureGestureBinding
import com.erasmus.upv.eps.wearables.model.BLEDevice
import com.erasmus.upv.eps.wearables.model.Gesture
import com.erasmus.upv.eps.wearables.viewModels.ReceivingDataViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber


class ConfigureGestureDialogFragment(private val device: BLEDevice, private val gesture: Gesture) : BottomSheetDialogFragment() {


    companion object {
        const val TAG = "ConfigureGestureDialogFragment"
    }

    private lateinit var binding: DialogFragmentConfigureGestureBinding
    private val viewModel: ReceivingDataViewModel by hiltNavGraphViewModels(R.id.receiving_data_nested_graph)



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
            Timber.d("Result ${viewModel.selectedTeam}, ${viewModel.selectedPlayer}, ${viewModel.selectedAction}")
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
        val players = mutableListOf<String>()
        players.addAll(viewModel.homeTeam.players.map { it.name })
        players.addAll(viewModel.guestTeam.players.map { it.name })
        val adapter = ArrayAdapter(requireContext(), R.layout.input_list_item, players)
        (binding.selectPlayerForGestureTf.editText as AutoCompleteTextView).setAdapter(adapter)
        (binding.selectPlayerForGestureTf.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
            viewModel.selectedAction = adapter.getItem(position).toString()
        }
    }

    private fun configSelectTeamInput() {
        val teams = listOf(viewModel.homeTeam.team.name, viewModel.guestTeam.team.name)
        val adapter = ArrayAdapter(requireContext(), R.layout.input_list_item, teams)
        (binding.selectTeamForGestureTf.editText as AutoCompleteTextView).setAdapter(adapter)
        (binding.selectTeamForGestureTf.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
            viewModel.selectedTeam = adapter.getItem(position).toString()
        }
    }

    private fun configSelectActionInput() {
        val actions = viewModel.getActionsForAMatch()
        val adapter = ArrayAdapter(requireContext(), R.layout.input_list_item, actions)
        (binding.selectActionTf.editText as AutoCompleteTextView).setAdapter(adapter)
        (binding.selectActionTf.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
            viewModel.selectedPlayer = adapter.getItem(position).toString()
        }
    }


}