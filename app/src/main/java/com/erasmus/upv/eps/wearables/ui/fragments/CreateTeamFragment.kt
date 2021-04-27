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
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.ui.adapters.PlayersShortAdapter
import com.erasmus.upv.eps.wearables.viewModels.CreateRelationsViewModel
import com.erasmus.upv.eps.wearables.viewModels.CreateTeamViewModel
import dagger.hilt.android.AndroidEntryPoint

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
        return binding.root
    }

    private fun receiveSafeArgs() {
        if(arguments != null){
            val args = CreateTeamFragmentArgs.fromBundle(requireArguments())
            if(args.teamId > 0L) {
                viewModel.receivedTeamId = args.teamId
                viewModel.getTeamWithPlayersById(viewModel.receivedTeamId).observe(viewLifecycleOwner) {
                    viewModel.teamWithPlayers = it
                    if(sharedViewModel.creatingTeam.teamId == 0L){
                        sharedViewModel.creatingTeam = it.team
                    }
                    sharedViewModel.addPlayersToTeamPlayers(viewModel.teamWithPlayers.players)
                    Log.i("CreateTeamFragment", "receiveSafeArgs: ${sharedViewModel.teamPlayers}")
                    Log.i("CreateTeamFragment", "receiveSafeArgs: ${it}")
                    adapter.notifyDataSetChanged()
                    populateInputs()
                    changeButtonText()
                }
            }
        }
    }

    private fun changeButtonText() {
        binding.saveTeamBt.text = "Update"
    }

    private fun populateInputs() {
        setPlayerSport()
        binding.teamNameEt.setText(sharedViewModel.creatingTeam.name)
        binding.teamCountryEt.setText(sharedViewModel.creatingTeam.country)
        binding.teamCityEt.setText(sharedViewModel.creatingTeam.city)
        binding.teamInfoEt.setText(sharedViewModel.creatingTeam.others)
        adapter.submitList(sharedViewModel.teamPlayers)
    }

    private fun setPlayerSport(): Int {
        return when(viewModel.teamWithPlayers!!.team.sport){
            "Football" -> R.id.football_radio_button
            "Basketball" -> R.id.basketball_radio_button
            else -> R.id.handball_radio_button
        }
    }


    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            sharedViewModel.isCreatingTeam = false
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
            if(viewModel.receivedTeamId == 0L) {
                viewModel.saveTeam(getUserInput())
                Toast.makeText(requireContext(), "Team created", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.updateTeam(getUserInput())
                sharedViewModel.updateTeamOfPlayers(viewModel.receivedTeamId)
                Toast.makeText(requireContext(), "Team Update", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

    private fun updatePlayers(){
        viewModel.teamId.observe(viewLifecycleOwner){
            sharedViewModel.updateTeamOfPlayers(it)
            findNavController().navigateUp()
          //  sharedViewModel.teamPlayers.clear()
            sharedViewModel.isCreatingTeam = false
        }
    }

    private fun getUserInput() : Team {
        return Team(
            viewModel.receivedTeamId,
            binding.teamNameEt.text.toString(),
            getSelectedSport(),
            Color.RED,
            binding.teamCountryEt.text.toString(),
            binding.teamCityEt.text.toString(),
            binding.teamInfoEt.text.toString()
        )
    }

    private fun getSelectedSport(): String  = when(binding.sportRadioGroup.checkedRadioButtonId){
        R.id.football_radio_button -> "Football"
        R.id.handball_radio_button -> "Handball"
        else -> "Basketball"
    }

    private fun setUpTeamPlayersRecyclerView() {
        val recyclerView = binding.teamPlayersRv
        adapter = PlayersShortAdapter()
        adapter.submitList(sharedViewModel.teamPlayers)
        Log.i("CreateTeamFragment", "setUpTeamPlayersRecyclerView: ${sharedViewModel.teamPlayers}")
        recyclerView.adapter = adapter
                recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }


}