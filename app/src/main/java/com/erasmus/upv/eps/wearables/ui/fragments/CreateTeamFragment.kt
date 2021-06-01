package com.erasmus.upv.eps.wearables.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentCreateTeamBinding
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.ui.adapters.PlayersShortAdapter
import com.erasmus.upv.eps.wearables.util.Sports
import com.erasmus.upv.eps.wearables.viewModels.CreateRelationsViewModel
import com.erasmus.upv.eps.wearables.viewModels.CreateTeamViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CreateTeamFragment : Fragment() {


    private lateinit var binding: FragmentCreateTeamBinding
    private val viewModel: CreateTeamViewModel by viewModels()
    private val sharedViewModel: CreateRelationsViewModel by activityViewModels()
    private lateinit var adapter: PlayersShortAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTeamBinding.inflate(inflater, container, false)

        setUpTeamPlayersRecyclerView()
        receiveSafeArgs()
        createTeam()
        addPlayer()
        handleBackPress()
        observeSportChange()
        return binding.root
    }

    private fun observeSportChange() {
        if(sharedViewModel.isCreatingTeam) {
            binding.sportRadioGroup.check(setPlayerSport(sharedViewModel.creatingTeam.sport))
        }
        binding.sportRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            Timber.d("selected sport ${getSelectedSport()} creaatig ${sharedViewModel.teamPlayers}")
            if(sharedViewModel.teamPlayers.any { it.sport != getSelectedSport() }) {
                sharedViewModel.teamPlayers.clear()
                adapter.submitList(sharedViewModel.teamPlayers)
            }
        }
    }

    private fun receiveSafeArgs() {
        if(arguments != null){
            val args = CreateTeamFragmentArgs.fromBundle(requireArguments())
            Timber.d("team ID ${args.teamId}")
            if(args.teamId > 0L) {
                viewModel.receivedTeamId = args.teamId
                viewModel.getTeamWithPlayersById(viewModel.receivedTeamId).observe(viewLifecycleOwner) {
                    viewModel.teamWithPlayers = it
                    if(sharedViewModel.creatingTeam.teamId == 0L){
                        sharedViewModel.creatingTeam = it.team
                        sharedViewModel.addPlayersToTeamPlayers(viewModel.teamWithPlayers.players)
                        adapter.submitList(sharedViewModel.teamPlayers)
                    }
                    populateInputs()
                    changeButtonText()
                }
            }
        }
    }

    private fun changeButtonText() {
        binding.saveTeamBt.text = getString(R.string.update)
    }

    private fun populateInputs() {
        binding.sportRadioGroup.check(setPlayerSport(sharedViewModel.creatingTeam.sport))
        binding.teamNameEt.setText(sharedViewModel.creatingTeam.name)
        binding.teamCountryEt.setText(sharedViewModel.creatingTeam.country)
        binding.teamCityEt.setText(sharedViewModel.creatingTeam.city)
        binding.teamInfoEt.setText(sharedViewModel.creatingTeam.others)
        adapter.submitList(sharedViewModel.teamPlayers)
    }

    private fun setPlayerSport(sport: String): Int {
        return when(sport){
            Sports.FOOTBALL -> R.id.football_radio_button
            Sports.BASKETBALL -> R.id.basketball_radio_button
            else -> R.id.handball_radio_button
        }
    }


    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            sharedViewModel.isCreatingTeam = false
            sharedViewModel.resetCreatingTeam()
            sharedViewModel.teamPlayers.clear()
            findNavController().navigateUp()
        }
    }

    private fun addPlayer() {
        binding.addPlayerBt.setOnClickListener {
            sharedViewModel.creatingTeam = getUserInput()
            val destination = CreateTeamFragmentDirections.actionCreateTeamFragmentToPlayersFragment(true)
            findNavController().navigate(destination)
        }
    }

    private fun createTeam() {
        updatePlayers()
        binding.saveTeamBt.setOnClickListener {
            sharedViewModel.resetCreatingTeam()
            if(viewModel.receivedTeamId == 0L) {
                viewModel.saveTeam(getUserInput())
                Toast.makeText(requireContext(), getString(R.string.team_created), Toast.LENGTH_SHORT).show()
            }else{
                viewModel.updateTeam(getUserInput())
                Toast.makeText(requireContext(), getString(R.string.team_updated), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updatePlayers(){
        viewModel.teamId.observe(viewLifecycleOwner){
            sharedViewModel.updateTeamOfPlayers(it, viewModel.teamWithPlayers.players)
            findNavController().navigateUp()
            sharedViewModel.isCreatingTeam = false
        }
    }

    private fun getUserInput() : Team {
        return Team(
            viewModel.receivedTeamId,
            binding.teamNameEt.text.toString(),
            getSelectedSport(),
            binding.teamCountryEt.text.toString(),
            binding.teamCityEt.text.toString(),
            binding.teamInfoEt.text.toString()
        )
    }

    private fun getSelectedSport(): String  = when(binding.sportRadioGroup.checkedRadioButtonId){
        R.id.football_radio_button -> Sports.FOOTBALL
        R.id.handball_radio_button -> Sports.HANDBALL
        else -> Sports.BASKETBALL
    }

    private fun setUpTeamPlayersRecyclerView() {
        val recyclerView = binding.teamPlayersRv
        adapter = PlayersShortAdapter(deletePlayer = this::deletePlayerFromAdapter)
        adapter.submitList(sharedViewModel.teamPlayers)
        recyclerView.adapter = adapter
                recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun deletePlayerFromAdapter(player: Player){
        sharedViewModel.teamPlayers.remove(player)
        adapter.submitList(sharedViewModel.teamPlayers)
    }



}