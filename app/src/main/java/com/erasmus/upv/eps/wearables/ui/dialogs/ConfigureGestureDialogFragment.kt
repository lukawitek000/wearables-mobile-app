package com.erasmus.upv.eps.wearables.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.navGraphViewModels
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.DialogFragmentConfigureGestureBinding
import com.erasmus.upv.eps.wearables.model.BLEDevice
import com.erasmus.upv.eps.wearables.model.Gesture
import com.erasmus.upv.eps.wearables.model.Player
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
            Timber.d("Result ${viewModel.selectedTeamId}, ${viewModel.selectedPlayerId}, ${viewModel.selectedAction}")
            gesture.assignPlayerId = viewModel.selectedPlayerId
            gesture.assignTeamId = viewModel.selectedTeamId
            gesture.action = viewModel.selectedAction
        }
        configDropDownInputs()

        return binding.root
    }

    private fun configDropDownInputs() {
        configSelectActionInput()
        configSelectTeamInput()
      //  configSelectPlayerInput()
    }

    private fun configSelectPlayerInput() {
        val players = mutableListOf<Player>()
        players.addAll(viewModel.getPlayersFromSelectedTeam())
        val playersAdapter = ArrayAdapter(requireContext(), R.layout.input_list_item, players)
        (binding.selectPlayerForGestureTf.editText as AutoCompleteTextView).setAdapter(playersAdapter)
        (binding.selectPlayerForGestureTf.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
            viewModel.selectedPlayerId = playersAdapter.getItem(position)?.playerId ?: 0L
        }
    }

    private fun configSelectTeamInput() {
        val teams = listOf(viewModel.homeTeam.team, viewModel.guestTeam.team)
        val adapter = ArrayAdapter(requireContext(), R.layout.input_list_item, teams)
        (binding.selectTeamForGestureTf.editText as AutoCompleteTextView).setAdapter(adapter)
        (binding.selectTeamForGestureTf.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
            viewModel.selectedTeamId = adapter.getItem(position)?.teamId ?: 0L
            binding.selectPlayerForGestureTf.isEnabled = true
            configSelectPlayerInput()
        }
    }

    private fun configSelectActionInput() {
        val actions = viewModel.getActionsForAMatch()
        val adapter = ArrayAdapter(requireContext(), R.layout.input_list_item, actions)
        (binding.selectActionTf.editText as AutoCompleteTextView).setAdapter(adapter)
        (binding.selectActionTf.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
            viewModel.selectedAction = adapter.getItem(position)
        }
    }





}