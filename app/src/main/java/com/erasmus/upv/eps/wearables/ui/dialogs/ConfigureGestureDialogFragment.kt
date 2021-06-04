package com.erasmus.upv.eps.wearables.ui.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.bumptech.glide.Glide
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.DialogFragmentConfigureGestureBinding
import com.erasmus.upv.eps.wearables.model.BLEDevice
import com.erasmus.upv.eps.wearables.model.Gesture
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.model.actions.Actions
import com.erasmus.upv.eps.wearables.model.actions.FootballActions
import com.erasmus.upv.eps.wearables.model.actions.NONE
import com.erasmus.upv.eps.wearables.ui.adapters.ActionsAdapter
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
        setGifForGesture()
        configDropDownInputs()
        configCheckBoxes()
        return binding.root
    }

    private fun setGifForGesture() {
        when(gesture.name){
            "Gesture 1" -> Glide.with(this).load(R.drawable.gesture_1).into(binding.gestureIv)
            "Gesture 2" -> Glide.with(this).load(R.drawable.gesture_2).into(binding.gestureIv)
            "Gesture 3" -> Glide.with(this).load(R.drawable.gesture_3).into(binding.gestureIv)
            "Gesture 4" -> Glide.with(this).load(R.drawable.gesture_4).into(binding.gestureIv)
            "Gesture 5" -> Glide.with(this).load(R.drawable.gesture_5).into(binding.gestureIv)
            "Gesture 6" -> Glide.with(this).load(R.drawable.gesture_6).into(binding.gestureIv)
            "Gesture 7" -> Glide.with(this).load(R.drawable.gesture_7).into(binding.gestureIv)
            "Gesture 8" -> Glide.with(this).load(R.drawable.gesture_8).into(binding.gestureIv)
            "Gesture 9" -> Glide.with(this).load(R.drawable.gesture_9).into(binding.gestureIv)
            "Gesture 10" -> Glide.with(this).load(R.drawable.gesture_10).into(binding.gestureIv)
            else -> Toast.makeText(requireContext(), "NO GIF", Toast.LENGTH_SHORT).show()
        }
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
        players.add("NONE")
        val playersAdapter = ArrayAdapter(requireContext(), R.layout.input_list_item, players)
        val editText = (binding.selectPlayerForGestureTf.editText as AutoCompleteTextView)
        editText.setAdapter(playersAdapter)
        editText.setOnItemClickListener { _, _, position, _ ->
            viewModel.selectedPlayerId = viewModel.getPlayerIdByPlayerName(playersAdapter.getItem(position))
        }
        if(gesture.assignPlayerId != 0L && gesture.action != null){
            viewModel.selectedPlayerId = gesture.assignPlayerId
            editText.setText(viewModel.getPlayerNameById(gesture.assignPlayerId), false)
        }
    }

    private fun configSelectTeamInput() {
        val teams = listOf(viewModel.homeTeam.team.name, viewModel.guestTeam.team.name, "NONE")
        val adapter = ArrayAdapter(requireContext(), R.layout.input_list_item, teams)
        val editText = (binding.selectTeamForGestureTf.editText as AutoCompleteTextView)
        editText.setAdapter(adapter)
        editText.setOnItemClickListener { _, _, position, _ ->
            viewModel.selectedTeamId = viewModel.getTeamIdByTeamName(adapter.getItem(position))
            binding.selectPlayerForGestureTf.isEnabled = true
            configSelectPlayerInput()
            resetPlayerInput()
        }
        if(gesture.assignTeamId != 0L && gesture.action != null){
            viewModel.selectedTeamId = gesture.assignTeamId
            editText.setText(viewModel.getTeamNameById(gesture.assignTeamId), false)
            configSelectPlayerInput()
        }
    }

    private fun resetPlayerInput(){
        (binding.selectPlayerForGestureTf.editText as AutoCompleteTextView).setText("", false)
    }

    private fun resetTeamInput(){
        (binding.selectTeamForGestureTf.editText as AutoCompleteTextView).setText("", false)
        resetPlayerInput()
    }

    private fun configSelectActionInput() {
        val actionsNames = viewModel.getActionsNamesForAMatch().toMutableList()
        actionsNames.add(NONE.NONE.name)
        Timber.d("action names $actionsNames")
        val actions = viewModel.getActionsForAMatch().toMutableList()
        actions.add(NONE.NONE)
        val adapter = ActionsAdapter(requireContext(), R.layout.input_list_item,  actionsNames.toTypedArray(), actions.toTypedArray())
        val editText = (binding.selectActionTf.editText as AutoCompleteTextView)
        editText.setAdapter(adapter)
        editText.setOnItemClickListener { _, _, position, _ ->
            val action = adapter.getItemAsAction(position)
            viewModel.selectedAction = if(action == NONE.NONE){
                resetTeamInput()
                null
            } else {
                action
            }
        }
        if(gesture.action != null){
            viewModel.selectedAction = gesture.action
            editText.setText(viewModel.getSelectedActionAsAText(), false)
        }
    }

    private fun configCheckBoxes() {
        Timber.d("should ask team ${gesture.shouldAskAboutTeam} player ${gesture.shouldAskAboutPlayer}")
        binding.doNotAskAboutTeamCb.isChecked = !gesture.shouldAskAboutTeam
        binding.doNotAskAboutPlayerCb.isChecked = !gesture.shouldAskAboutPlayer
        setCheckBoxesListeners()
    }

    private fun setCheckBoxesListeners() {
        binding.doNotAskAboutTeamCb.setOnCheckedChangeListener { _, isChecked ->
            Timber.d("ask about team ${gesture.shouldAskAboutTeam} is CHecked = $isChecked")
            gesture.shouldAskAboutTeam = !isChecked
        }
        binding.doNotAskAboutPlayerCb.setOnCheckedChangeListener { _, isChecked ->
            Timber.d("should ask about player ${gesture.shouldAskAboutPlayer} is CHecked = $isChecked ")
            gesture.shouldAskAboutPlayer = !isChecked
        }
    }


}
