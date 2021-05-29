package com.erasmus.upv.eps.wearables.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentCurrentMatchBinding
import com.erasmus.upv.eps.wearables.model.LiveAction
import com.erasmus.upv.eps.wearables.service.BLEConnectionForegroundService
import com.erasmus.upv.eps.wearables.ui.adapters.LiveActionsAdapter
import com.erasmus.upv.eps.wearables.ui.dialogs.SelectPlayerDialogFragment
import com.erasmus.upv.eps.wearables.ui.dialogs.SelectTeamDialogFragment
import com.erasmus.upv.eps.wearables.util.BLEConnectionManager
import com.erasmus.upv.eps.wearables.util.DateTimeFormatter
import com.erasmus.upv.eps.wearables.util.MatchTimer
import com.erasmus.upv.eps.wearables.viewModels.ReceivingDataViewModel
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import timber.log.Timber


class CurrentMatchFragment : Fragment() {

    private lateinit var liveActionsAdapter: LiveActionsAdapter
    private lateinit var binding: FragmentCurrentMatchBinding

    private val viewModel: ReceivingDataViewModel by hiltNavGraphViewModels(R.id.receiving_data_nested_graph)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentCurrentMatchBinding.inflate(
                inflater, container, false
        )


        populateUi()
        setUpCustomBackPress()
        mockDialogsForChoosingPlayersAndTeams()
        setUpRecyclerView()
        handleStartMatchTimerButton()
        showMatchTime()

        observeReceivedData()
        askForATeam()
        askForAPlayer()
        observeRecordedLiveAction()
        observeConnectedDevices()


        binding.numberOfConnectedDevicesTv.setOnClickListener {
            BLEConnectionManager.readGesture()
        }



        if(!BLEConnectionForegroundService.isServiceRunning) {
            sendCommandToBLEConnectionService(BLEConnectionForegroundService.START)
        }

        return binding.root
    }

    private fun observeConnectedDevices() {
        BLEConnectionManager.isConnectionChanged.observe(viewLifecycleOwner){
            if(it){
                Timber.d("Connected devices: ${BLEConnectionManager.getBluetoothConnectedGatts()}")
                binding.numberOfConnectedDevicesTv.text = BLEConnectionManager.getBluetoothConnectedGatts().size.toString()
            }
        }
    }


    private fun observeRecordedLiveAction() {
        viewModel.getLiveActionsForCurrentMatch().observe(viewLifecycleOwner){
            Timber.d("Live actions = $it")
            liveActionsAdapter.submitList(it.toMutableList())
        }
    }

    private fun askForAPlayer() {
        viewModel.askedPlayerId.observe(viewLifecycleOwner){
            if(it == 0L && !viewModel.isDataCleared){
                SelectPlayerDialogFragment().show(childFragmentManager, SelectPlayerDialogFragment.TAG)
            }
        }
    }

    private fun askForATeam() {
        viewModel.askedTeamId.observe(viewLifecycleOwner){
            if(it == 0L && !viewModel.isDataCleared){
                SelectTeamDialogFragment().show(childFragmentManager, SelectTeamDialogFragment.TAG)
            }
        }
    }

    private fun observeReceivedData() {
        BLEConnectionForegroundService.receiveData.observe(viewLifecycleOwner) {
            Timber.i("onCreateView: data changed $it")
            if (it != null && it.isNotEmpty()) {

                viewModel.addNewLiveAction(it.first())

            }

        }
    }

    private fun populateUi() {
        binding.homeTeamNameTv.text = viewModel.homeTeam.team.name
        binding.guestTeamNameTv.text = viewModel.guestTeam.team.name
        binding.leagueNameTv.text = viewModel.match.league
    }

    private fun showMatchTime() {
        MatchTimer.matchTimeInMillis.observe(viewLifecycleOwner){
            binding.matchTimerTv.text = DateTimeFormatter.displayMinutesAndSeconds(it)
            if(it <= binding.matchTimeSl.valueTo) {
                binding.matchTimeSl.value = (it).toFloat()
            }
        }
        showMatchCompleted()
        handlePauseMatchTime()
        initTimeSlider()
        timeSliderListener()

    }

    private fun showMatchCompleted() {
        MatchTimer.isMatchIntervalCompleted.observe(viewLifecycleOwner) {
            if (it) {
                if (MatchTimer.intervalsLeft > 0) {
                    binding.startMatchBt.text = "Start next interval"
                    binding.pauseTimerBt.isEnabled = false
                } else {
                    binding.startMatchBt.text = "Restart"
                    binding.pauseTimerBt.isEnabled = false
                }
            }
        }
    }

    private fun handlePauseMatchTime() {
        binding.pauseTimerBt.isEnabled = false
        binding.pauseTimerBt.setOnClickListener {
            if (MatchTimer.isTimerPaused) {
                MatchTimer.resumeTimer()
                setPauseTimerButtonText()
            } else {
                MatchTimer.pauseTimer()
                setPauseTimerButtonText()
            }
        }
    }

    private fun setPauseTimerButtonText() {
        binding.pauseTimerBt.text = if(MatchTimer.isTimerPaused) "Resume" else "Pause"
    }

    private fun timeSliderListener() {
        binding.matchTimeSl.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
               Timber.d("match time slider start touch ${slider.value}")
            }

            override fun onStopTrackingTouch(slider: Slider) {
                Timber.d("match time slider changes end ${slider.value}")
                MatchTimer.setTimerValue(slider.value)
            }
        })
    }

    private fun initTimeSlider() {
        binding.matchTimeSl.value = 0.0f
        val valueTo = (viewModel.match.matchPartDuration * 60 * 1000L * viewModel.match.matchParts).toFloat()
        binding.matchTimeSl.valueTo = if(valueTo <= 0.0f){
            1000f
        }else{
            valueTo
        }
        binding.matchTimeSl.stepSize = 1000f
        binding.matchTimeSl.setLabelFormatter{
            DateTimeFormatter.displayMinutesAndSeconds(it.toLong())
        }
    }

    private fun handleStartMatchTimerButton(){
        binding.startMatchBt.setOnClickListener {
            if(MatchTimer.isTimerRunning()){
                MatchTimer.stopTimer()
                binding.startMatchBt.text = "START MATCH"
                binding.pauseTimerBt.isEnabled = false
                setPauseTimerButtonText()
            }else {
                if(!MatchTimer.isMatchIntervalCompleted.value!!) {
                    MatchTimer.configTimer(viewModel.match.matchPartDuration * 60, viewModel.match.matchParts)
                }
                restartMatchTimer()
                MatchTimer.startTimer()
                binding.startMatchBt.text = "STOP MATCH"
                binding.pauseTimerBt.isEnabled = true
            }
        }
    }

    private fun restartMatchTimer() {
        if (MatchTimer.intervalsLeft == 0) {
            MatchTimer.resetTimer()
            MatchTimer.configTimer(viewModel.match.matchPartDuration * 60, viewModel.match.matchParts)
        }
    }


    private fun setUpCustomBackPress() {
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                buildBackPressedAlertDialog().show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


    private fun buildBackPressedAlertDialog(): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Warning")
        builder.setMessage("If you left this screen the statistics recording will fail. Do you want to exit?")
        builder.apply {
            setPositiveButton("Yes") { _, _ ->
                stopService()
                findNavController().navigate(R.id.action_currentMatchFragment_to_matchesFragment)
                MatchTimer.stopTimer()
            }
            setNegativeButton("No" ){ dialog, _ ->
                dialog.dismiss()
            }
        }
        return builder.create()
    }

    private fun mockDialogsForChoosingPlayersAndTeams() {
//        binding.simulateWhichTeamBn.setOnClickListener {
//            SelectTeamDialogFragment().show(childFragmentManager, SelectTeamDialogFragment.TAG)
//        }
//
//
//        binding.simulateWhichPlayerBn.setOnClickListener {
//            SelectPlayerDialogFragment().show(childFragmentManager, SelectPlayerDialogFragment.TAG)
//        }
    }

    private fun setUpRecyclerView() {
        val rv = binding.liveActionsRv
        rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        liveActionsAdapter = LiveActionsAdapter(
            this::deleteLiveAction,
            viewModel::getTeamColor,
            viewModel::getPlayerNameById,
            viewModel::getTeamNameById
        )
        liveActionsAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                rv.smoothScrollToPosition(0)
            }
        })
        rv.adapter = liveActionsAdapter
    }

    private fun deleteLiveAction(liveAction: LiveAction){
        viewModel.deleteLiveActionById(liveAction.id)
        //liveActionsAdapter.notifyDataSetChanged()
    }


    private fun stopService() {
        BLEConnectionForegroundService.isServiceRunning = false
        sendCommandToBLEConnectionService(BLEConnectionForegroundService.STOP)
    }



    private fun sendCommandToBLEConnectionService(command: String){
        Intent(requireContext(), BLEConnectionForegroundService::class.java).apply {
            this.action = command
        }.also { intent ->
            requireActivity().startService(intent)
        }
    }


}
