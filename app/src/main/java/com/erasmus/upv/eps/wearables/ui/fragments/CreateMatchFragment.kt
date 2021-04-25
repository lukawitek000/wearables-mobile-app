package com.erasmus.upv.eps.wearables.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentCreateMatchBinding
import com.erasmus.upv.eps.wearables.databinding.ItemViewTeamBinding
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.util.DateTimeFormatter
import com.erasmus.upv.eps.wearables.util.TeamCreated
import com.erasmus.upv.eps.wearables.viewModels.CreateMatchViewModel
import com.erasmus.upv.eps.wearables.viewModels.CreateRelationsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CreateMatchFragment : Fragment() {

    private lateinit var binding: FragmentCreateMatchBinding
    private val sharedViewModel: CreateRelationsViewModel by activityViewModels()

    private val viewModel: CreateMatchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateMatchBinding.inflate(inflater, container, false)

        handleDateInput()
        handleTimeInput()
        createMatch()
        observeCreatedMatchId()
        addHomeTeam()
        addGuestTeam()

        binding.doneCreatingMatchFb.isEnabled = sharedViewModel.areBothTeamsAdded()
        populateTeamLayout(sharedViewModel.homeTeam, binding.homeTeamSelected)
        populateTeamLayout(sharedViewModel.guestTeam, binding.guestTeamSelected)

        return binding.root
    }



    private fun populateTeamLayout(team: Team?, teamSelectedLayout: ItemViewTeamBinding) {
        if(team == null){
            teamSelectedLayout.itemViewTeamLayout.visibility = View.GONE
            return
        }
        teamSelectedLayout.itemViewTeamLayout.visibility = View.VISIBLE
        teamSelectedLayout.teamNameAdapterTv.text = team.name
        teamSelectedLayout.teamCityAdapterTv.text = team.city
    }


    private fun addGuestTeam() {
        binding.chooseGuestTeamBt.setOnClickListener {
            sharedViewModel.whichTeamIsCreated = TeamCreated.GUEST_TEAM
            findNavController().navigate(R.id.action_createMatchFragment_to_teamsFragment)
        }
    }

    private fun addHomeTeam() {
        binding.chooseHomeTeamBt.setOnClickListener {
            sharedViewModel.whichTeamIsCreated = TeamCreated.HOME_TEAM
            findNavController().navigate(R.id.action_createMatchFragment_to_teamsFragment)
        }
    }

    private fun createMatch() {
        binding.doneCreatingMatchFb.setOnClickListener {
            getMatchFromUserInput()
            viewModel.insertMatch()
        }
    }

    private fun observeCreatedMatchId() {
        viewModel.matchId.observe(viewLifecycleOwner){
            sharedViewModel.createMatchTeamCrossRef(it)
            Toast.makeText(requireContext(), "Match created", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    private fun getMatchFromUserInput() {
        viewModel.match.location = binding.matchLocationEt.text.toString()
        viewModel.match.sport = getSelectedSport()
        viewModel.match.league = binding.matchLeagueEt.text.toString()
        viewModel.match.otherDetails = binding.matchDetailsEt.text.toString()
    }

    private fun getSelectedSport(): String  = when(binding.sportRadioGroup.checkedRadioButtonId){
        R.id.football_radio_button -> "Football"
        R.id.handball_radio_button -> "Handball"
        else -> "Basketball"
    }

    private fun handleTimeInput() {
        binding.matchTimeEt.setOnClickListener {
            createTimePicker()
        }
    }


    private fun handleDateInput() {
        binding.matchDateEt.setOnClickListener {
            createDataPicker()
        }
    }


    private fun createTimePicker() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                setMatchTime(hourOfDay, minute)
                binding.matchTimeEt.setText(DateTimeFormatter.displayTime(viewModel.matchDate.timeInMillis))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun setMatchTime(hourOfDay: Int, minute: Int) {
        viewModel.matchDate.set(
            viewModel.matchDate.get(Calendar.YEAR),
            viewModel.matchDate.get(Calendar.MONTH),
            viewModel.matchDate.get(Calendar.DAY_OF_MONTH),
            hourOfDay,
            minute
            )
    }

    private fun createDataPicker() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                viewModel.matchDate.set(year, month, dayOfMonth)
                binding.matchDateEt.setText(DateTimeFormatter.displayDate(viewModel.matchDate.timeInMillis))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = Date().time
        datePickerDialog.show()

    }


}