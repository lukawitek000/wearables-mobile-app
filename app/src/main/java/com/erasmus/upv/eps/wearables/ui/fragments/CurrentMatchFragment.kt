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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentCurrentMatchBinding
import com.erasmus.upv.eps.wearables.model.LiveAction
import com.erasmus.upv.eps.wearables.service.BLEConnectionForegroundService
import com.erasmus.upv.eps.wearables.ui.adapters.LiveActionsAdapter
import com.erasmus.upv.eps.wearables.ui.dialogs.SelectPlayerDialogFragment
import com.erasmus.upv.eps.wearables.ui.dialogs.SelectTeamDialogFragment
import timber.log.Timber


class CurrentMatchFragment : Fragment() {

    private lateinit var binding: FragmentCurrentMatchBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentCurrentMatchBinding.inflate(
                inflater, container, false
        )


        setUpCustomBackPress()
        mockDialogsForChoosingPlayersAndTeams()
        setUpRecyclerView()

//        BLEConnectionForegroundService.receiveData.observe(viewLifecycleOwner){
//            Timber.i( "onCreateView: data changed $it")
//        }
        if(!BLEConnectionForegroundService.isServiceRunning) {
            sendCommandToBLEConnectionService(BLEConnectionForegroundService.START)
        }

        return binding.root
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
        rv.adapter = LiveActionsAdapter(liveActions)
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


private val liveActions = listOf<LiveAction>(
        LiveAction("1:11", "Goal", "none"),
        LiveAction("21:11", "Foul", "none"),
        LiveAction("1:22", "Offside", "sdfds"),
        LiveAction("9:51", "Goal", "none"),
        LiveAction("7:00", "Assist", "gdg"),
        LiveAction("1:44", "Foul", "none"),
        LiveAction("12:11", "Goal", "dfgdf"),
        LiveAction("90:11", "Offside", "none"),
        LiveAction("13:11", "Save", "none"),
        LiveAction("51:11", "Assist", "egeg"),


)