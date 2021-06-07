package com.erasmus.upv.eps.wearables.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentCreateMatchBinding
import com.erasmus.upv.eps.wearables.databinding.ItemViewTeamBinding
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.util.DateTimeFormatter
import com.erasmus.upv.eps.wearables.util.Sports
import com.erasmus.upv.eps.wearables.util.TeamCreated
import com.erasmus.upv.eps.wearables.viewModels.CreateMatchViewModel
import com.erasmus.upv.eps.wearables.viewModels.CreateRelationsViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.Exception
import java.util.*

// TODO check sport when updating
// TODO show teams when updating


@AndroidEntryPoint
class CreateMatchFragment : Fragment() {

    private lateinit var binding: FragmentCreateMatchBinding
    private val sharedViewModel: CreateRelationsViewModel by activityViewModels()
    private val viewModel: CreateMatchViewModel by viewModels()


    companion object {
        private val green = Color.rgb(0, 159, 150)
        private val red = Color.rgb(229, 0, 64)
        private val blue =  Color.rgb(75, 72, 239)
        private val yellow = Color.rgb(231, 169, 12)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateMatchBinding.inflate(inflater, container, false)

        receiveSafeArgs()
        handleDateInput()
        handleTimeInput()
        createMatch()
        observeCreatedMatchId()
        addHomeTeam()
        addGuestTeam()

        enableSavingMatchButton()
        populateTeamsLayouts()

        customBackPress()
        observeSportChange()

        return binding.root
    }

    private fun observeSportChange() {
        binding.radioButtonLayout.sportRadioGroup.check(setSportRadioButton(sharedViewModel.creatingMatch.sport))
        binding.radioButtonLayout.sportRadioGroup.setOnCheckedChangeListener { _, chekedId ->
            sharedViewModel.creatingMatch.sport = getSelectedSport()

            Timber.d("sport change $chekedId ${getSelectedSport()} home ${sharedViewModel.homeTeam?.sport} guest ${sharedViewModel.guestTeam?.sport}")
            if(getSelectedSport() != sharedViewModel.homeTeam?.sport){
                sharedViewModel.homeTeam = null
            }
            if(getSelectedSport() != sharedViewModel.guestTeam?.sport) {
                sharedViewModel.guestTeam = null
            }
            populateTeamsLayouts()
        }
    }


    private fun enableSavingMatchButton() {
        binding.doneCreatingMatchFb.isEnabled = sharedViewModel.areBothTeamsAdded()
    }

    private fun populateTeamsLayouts() {
        populateTeamLayout(sharedViewModel.homeTeam, binding.homeTeamSelected)
        populateTeamLayout(sharedViewModel.guestTeam, binding.guestTeamSelected)
    }

    private fun receiveSafeArgs() {
        if(arguments != null){
            val args = CreateMatchFragmentArgs.fromBundle(requireArguments())
            viewModel.receivedMatchId = args.matchId
            if(viewModel.receivedMatchId != 0L) {
                changeButtonIcon()
                getMatchDetails()
            }
        }
    }

    private fun getMatchDetails() {
        viewModel.getMatchDetails().observe(viewLifecycleOwner){
            viewModel.matchWithTeams = it
            if(sharedViewModel.creatingMatch.matchId == 0L) {
                sharedViewModel.creatingMatch = it.match
            }
            populateUi()
        }
    }

    private fun populateUi() {
        setEditTexts(sharedViewModel.creatingMatch)
        setTeamsToSharedViewModel()
        populateTeamsLayouts()
        setTeamColors()
        binding.doneCreatingMatchFb.isEnabled = true
        binding.radioButtonLayout.sportRadioGroup.check(setSportRadioButton(sharedViewModel.creatingMatch.sport))
    }

    private fun setTeamColors() {
        binding.homeTeamColorCg.check(getHomeTeamColorChipId())
        binding.guestTeamColorCg.check(getGuestTeamColorChipId())
    }

    private fun getGuestTeamColorChipId(): Int {
        return when (sharedViewModel.creatingMatch.guestTeamColor) {
            red -> R.id.guest_team_color_red_c
            blue -> R.id.guest_team_color_blue_c
            green -> R.id.guest_team_color_green_c
            else -> R.id.guest_team_color_yellow_c
        }
    }

    private fun getHomeTeamColorChipId(): Int {
        return when (sharedViewModel.creatingMatch.homeTeamColor) {
            red -> R.id.home_team_color_red_c
            blue -> R.id.home_team_color_blue_c
            yellow -> R.id.home_team_color_yellow_c
            else -> R.id.home_team_color_green_c
        }
    }

    private fun setTeamsToSharedViewModel() {
        if (sharedViewModel.homeTeam == null) {
            sharedViewModel.homeTeam = viewModel.matchWithTeams.teams[0]
        }
        if (sharedViewModel.guestTeam == null) {
            sharedViewModel.guestTeam = viewModel.matchWithTeams.teams[1]
        }
    }


    private fun setEditTexts(match: Match) {
        binding.matchDateEt.setText(DateTimeFormatter.displayDate(match.date.time))
        binding.matchTimeEt.setText(DateTimeFormatter.displayTime(match.date.time))
        binding.matchLocationEt.setText(match.location)
        binding.matchCityEt.setText(match.city)
        binding.matchLeagueEt.setText(match.league)
        binding.matchDetailsEt.setText(match.otherDetails)
        binding.matchPartsDurationEt.setText(match.matchPartDuration.toString())
        binding.matchPartsNumberEt.setText(match.matchParts.toString())
    }

    private fun setSportRadioButton(sport: String): Int {
        return when(sport){
            Sports.FOOTBALL -> R.id.football_radio_button
            Sports.BASKETBALL -> R.id.basketball_radio_button
            else -> R.id.handball_radio_button
        }
    }

    private fun changeButtonIcon() {
        binding.doneCreatingMatchFb.setImageResource(R.drawable.ic_update)
    }

    private fun customBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            sharedViewModel.clearTeams()
            findNavController().navigateUp()
        }
    }


    private fun populateTeamLayout(team: Team?, teamSelectedLayout: ItemViewTeamBinding) {
        if(team == null){
            teamSelectedLayout.itemViewTeamLayout.visibility = View.INVISIBLE
            return
        }
        teamSelectedLayout.itemViewTeamLayout.visibility = View.VISIBLE
        teamSelectedLayout.teamNameAdapterTv.text = team.name
        teamSelectedLayout.teamCityAdapterTv.text = team.city
    }


    private fun addGuestTeam() {
        binding.chooseGuestTeamBt.setOnClickListener {
            getMatchFromUserInput()
            sharedViewModel.whichTeamIsCreated = TeamCreated.GUEST_TEAM
            findNavController().navigate(R.id.action_createMatchFragment_to_teamsFragment)
        }
    }

    private fun addHomeTeam() {
        binding.chooseHomeTeamBt.setOnClickListener {
            getMatchFromUserInput()
            sharedViewModel.whichTeamIsCreated = TeamCreated.HOME_TEAM
            findNavController().navigate(R.id.action_createMatchFragment_to_teamsFragment)
        }
    }

    private fun createMatch() {
        binding.doneCreatingMatchFb.setOnClickListener {
            getMatchFromUserInput()
            if(viewModel.receivedMatchId == 0L) {
                viewModel.insertMatch(sharedViewModel.creatingMatch)
                Toast.makeText(requireContext(), getString(R.string.match_created), Toast.LENGTH_SHORT).show()
            }else{
                viewModel.updateMatch(sharedViewModel.creatingMatch)
                Toast.makeText(requireContext(), getString(R.string.match_updated), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeCreatedMatchId() {
        viewModel.matchId.observe(viewLifecycleOwner){
            sharedViewModel.createMatchTeamCrossRef(it)
            findNavController().navigateUp()
        }
    }

    private fun getMatchFromUserInput() {
        sharedViewModel.creatingMatch.location = binding.matchLocationEt.text.toString()
        sharedViewModel.creatingMatch.city = binding.matchCityEt.text.toString()
        sharedViewModel.creatingMatch.sport = getSelectedSport()
        sharedViewModel.creatingMatch.league = binding.matchLeagueEt.text.toString()
        sharedViewModel.creatingMatch.otherDetails = binding.matchDetailsEt.text.toString()
        sharedViewModel.creatingMatch.homeTeamColor = getHomeTeamColorFromChips()
        sharedViewModel.creatingMatch.guestTeamColor = getGuestTeamColorFromChips()
        sharedViewModel.creatingMatch.matchParts = getNumberOfMatchParts()
        sharedViewModel.creatingMatch.matchPartDuration = getMatchPartDuration()
    }

    private fun getMatchPartDuration(): Long {
        return try {
            binding.matchPartsDurationEt.text.toString().toLong()
        }catch (e: Exception){
            0L
        }
    }

    private fun getNumberOfMatchParts(): Int {
        return try {
            binding.matchPartsNumberEt.text.toString().toInt()
        }catch (e: Exception){
            0
        }
    }

    private fun getGuestTeamColorFromChips(): Int {
        return when(binding.guestTeamColorCg.checkedChipId){
            R.id.guest_team_color_red_c -> red
            R.id.guest_team_color_blue_c -> blue
            R.id.guest_team_color_green_c -> green
            else -> yellow
        }
    }

    private fun getHomeTeamColorFromChips(): Int {
        return when(binding.homeTeamColorCg.checkedChipId){
            R.id.home_team_color_red_c -> red
            R.id.home_team_color_blue_c -> blue
            R.id.home_team_color_yellow_c -> yellow
            else -> green
        }
    }

    private fun getSelectedSport(): String  = when(binding.radioButtonLayout.sportRadioGroup.checkedRadioButtonId){
        R.id.football_radio_button -> Sports.FOOTBALL
        R.id.handball_radio_button -> Sports.HANDBALL
        else -> Sports.BASKETBALL
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