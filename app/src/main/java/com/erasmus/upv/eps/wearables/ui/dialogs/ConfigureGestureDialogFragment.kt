package com.erasmus.upv.eps.wearables.ui.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.DialogFragmentConfigureGestureBinding
import com.erasmus.upv.eps.wearables.model.BLEDevice
import com.erasmus.upv.eps.wearables.model.Gesture
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.viewModels.ReceivingDataViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber
import java.util.*


class ConfigureGestureDialogFragment(
    private val device: BLEDevice,
    private val gesture: Gesture,
    private val updateColors: () -> Unit) : BottomSheetDialogFragment() {


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
            Timber.d("Result ${viewModel.selectedTeamId}, ${viewModel.selectedPlayerId}, ${viewModel.selectedAction}")
            gesture.assignPlayerId = viewModel.selectedPlayerId
            gesture.assignTeamId = viewModel.selectedTeamId
            gesture.action = viewModel.selectedAction
            dismiss()
        }
        configDropDownInputs()

        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        updateColors.invoke()
        viewModel.clearSelectedConfig()
    }

    private fun configDropDownInputs() {
        configSelectActionInput()
        configSelectTeamInput()
      //  configSelectPlayerInput()
    }

    private fun configSelectPlayerInput() {
        val players = mutableListOf<String>()
        players.addAll(viewModel.getPlayersNameFromSelectedTeam())
        val playersAdapter = ArrayAdapter(requireContext(), R.layout.input_list_item, players)
        val editText = (binding.selectPlayerForGestureTf.editText as AutoCompleteTextView)
        editText.setAdapter(playersAdapter)
        editText.setOnItemClickListener { _, _, position, _ ->
            viewModel.selectedPlayerId = viewModel.getPlayerIdByPlayerName(playersAdapter.getItem(position))
        }
        if(gesture.assignPlayerId != 0L){
            viewModel.selectedPlayerId = gesture.assignPlayerId
            editText.setText(viewModel.getPlayerNameById(gesture.assignPlayerId), false)
        }
    }

    private fun configSelectTeamInput() {
        val teams = listOf(viewModel.homeTeam.team.name, viewModel.guestTeam.team.name)
        val adapter = ArrayAdapter(requireContext(), R.layout.input_list_item, teams)
        val editText = (binding.selectTeamForGestureTf.editText as AutoCompleteTextView)
        editText.setAdapter(adapter)
        editText.setOnItemClickListener { _, _, position, _ ->
            viewModel.selectedTeamId = viewModel.getTeamIdByTeamName(adapter.getItem(position))
            binding.selectPlayerForGestureTf.isEnabled = true
            configSelectPlayerInput()
            (binding.selectPlayerForGestureTf.editText as AutoCompleteTextView).setText("", false)
        }
        if(gesture.assignTeamId != 0L){
            viewModel.selectedTeamId = gesture.assignTeamId
            editText.setText(viewModel.getTeamNameById(gesture.assignTeamId), false)
            configSelectPlayerInput()
        }
    }

    private fun configSelectActionInput() {
        val actions = viewModel.getActionsForAMatch()
        val adapter = ArrayAdapter(requireContext(), R.layout.input_list_item, actions)
        val editText = (binding.selectActionTf.editText as AutoCompleteTextView)
        editText.setAdapter(adapter)
        editText.setOnItemClickListener { _, _, position, _ ->
            viewModel.selectedAction = adapter.getItem(position)
        }
        if(gesture.action != null){
            viewModel.selectedAction = gesture.action
            editText.setText(viewModel.selectedAction.toString(), false)
        }
    }





}