package com.erasmus.upv.eps.wearables.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
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
import com.erasmus.upv.eps.wearables.databinding.FragmentCreatePlayerBinding
import com.erasmus.upv.eps.wearables.databinding.ItemViewTeamBinding
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.util.DateTimeFormatter
import com.erasmus.upv.eps.wearables.util.TeamCreated
import com.erasmus.upv.eps.wearables.viewModels.CreateMatchViewModel
import com.erasmus.upv.eps.wearables.viewModels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CreateMatchFragment : Fragment() {

    private lateinit var binding: FragmentCreateMatchBinding
    private val viewModel: CreateMatchViewModel by activityViewModels()
   // private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateMatchBinding.inflate(inflater, container, false)

        handleDateInput()
        handleTimeInput()
        createMatch()
        addHomeTeam()
        addGuestTeam()

        binding.doneCreatingMatchFb.isEnabled = viewModel.areBothTeamsAdded()
        populateTeamLayout(viewModel.homeTeam, binding.homeTeamSelected)
        populateTeamLayout(viewModel.guestTeam, binding.guestTeamSelected)

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
            viewModel.match = getMatchFromUserInput()
            val destination = CreateMatchFragmentDirections.actionCreateMatchFragmentToTeamsFragment(TeamCreated.GUEST_TEAM)
            findNavController().navigate(destination)
        }
    }

    private fun addHomeTeam() {
        binding.chooseHomeTeamBt.setOnClickListener {
            viewModel.match = getMatchFromUserInput()
            val destination = CreateMatchFragmentDirections.actionCreateMatchFragmentToTeamsFragment(TeamCreated.HOME_TEAM)
            findNavController().navigate(destination)
        }
    }

    private fun createMatch() {
        binding.doneCreatingMatchFb.setOnClickListener {
            viewModel.createMatch(getMatchFromUserInput())
            Toast.makeText(requireContext(), "Match created", Toast.LENGTH_SHORT).show()
            Log.i("CreateMatchFragment", "createMatch: ${viewModel.match} \n- home team ${viewModel.homeTeam} \n -guest team ${viewModel.guestTeam}")
            findNavController().navigateUp()
        }
    }

    private fun getMatchFromUserInput(): Match {
        return Match(
            0L,
            Date(),
            binding.matchLocationEt.text.toString(),
            getSelectedSport(),
            binding.matchLeagueEt.text.toString(),
            binding.matchDetailsEt.text.toString()
        )
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
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
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