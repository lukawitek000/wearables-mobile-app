package com.erasmus.upv.eps.wearables.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentCurrentMatchBinding
import com.erasmus.upv.eps.wearables.model.LiveAction
import com.erasmus.upv.eps.wearables.model.Response
import com.erasmus.upv.eps.wearables.service.BLEConnectionForegroundService
import com.erasmus.upv.eps.wearables.ui.adapters.LiveActionsAdapter
import com.erasmus.upv.eps.wearables.ui.dialogs.SelectPlayerDialogFragment
import com.erasmus.upv.eps.wearables.ui.dialogs.SelectTeamDialogFragment
import com.erasmus.upv.eps.wearables.util.DateTimeFormatter
import com.erasmus.upv.eps.wearables.viewModels.ReceivingDataViewModel
import timber.log.Timber
import java.util.*
import kotlin.concurrent.timer
import kotlin.time.minutes


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
        //handleStartMatchButton()
        showMatchTime()
        BLEConnectionForegroundService.matchStartTime = System.currentTimeMillis()

        observeReceivedData()
        askForATeam()
        observeRecordedLiveAction()



        if(!BLEConnectionForegroundService.isServiceRunning) {
            sendCommandToBLEConnectionService(BLEConnectionForegroundService.START)
        }

        return binding.root
    }

    private fun observeRecordedLiveAction() {
        viewModel.liveActions.observe(viewLifecycleOwner){
            Timber.d("Live actions = $it")
            liveActionsAdapter.submitList(it)
        }
    }

    private fun askForATeam() {
        viewModel.askedTeamId.observe(viewLifecycleOwner){
            if(it == 0L){
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
        BLEConnectionForegroundService.matchTime.observe(viewLifecycleOwner){
            binding.matchTimerTv.text = DateTimeFormatter.displayMinutesAndSeconds(it)
        }
    }

//    private fun handleStartMatchButton() {
//        binding.startMatchBt.setOnClickListener {
//            if(BLEConnectionForegroundService.matchTime.value == null || BLEConnectionForegroundService.matchTime.value == 0L) {
//                BLEConnectionForegroundService.createTimer(90)
//                BLEConnectionForegroundService.startTimer()
//                binding.startMatchBt.text = "STOP MATCH"
//                BLEConnectionForegroundService.matchStartTime = System.currentTimeMillis()
//            }else{
//                BLEConnectionForegroundService.cancelTimer()
//                binding.startMatchBt.text = "START MATCH"
//            }
//        }
//    }


//
//    private fun convertReceivedDataToLiveActions(it: MutableList<Response>) =
//            it.map { response ->
//                val matchTime = Date(response.timeStamp - BLEConnectionForegroundService.matchStartTime)
//                LiveAction(DateTimeFormatter.displayMinutesAndSeconds(matchTime.time), response.data.toString(), response.device.name)
//            }

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
                Toast.makeText(requireContext(), "Go back", Toast.LENGTH_SHORT).show()
            }
            setNegativeButton("No" ){ dialog, _ ->
                dialog.dismiss()
            }
        }
        return builder.create()
    }

    private fun mockDialogsForChoosingPlayersAndTeams() {
        binding.simulateWhichTeamBn.setOnClickListener {
            SelectTeamDialogFragment().show(childFragmentManager, SelectTeamDialogFragment.TAG)
        }


        binding.simulateWhichPlayerBn.setOnClickListener {
            SelectPlayerDialogFragment().show(childFragmentManager, SelectPlayerDialogFragment.TAG)
        }
    }

    private fun setUpRecyclerView() {
        val rv = binding.liveActionsRv
        rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        liveActionsAdapter = LiveActionsAdapter()
        rv.adapter = liveActionsAdapter
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
