package com.erasmus.upv.eps.wearables.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentCreateTeamBinding
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.ui.adapters.PlayersShortAdapter
import com.erasmus.upv.eps.wearables.viewModels.CreateMatchViewModel
import com.erasmus.upv.eps.wearables.viewModels.CreateTeamViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateTeamFragment : Fragment() {


    private lateinit var binding: FragmentCreateTeamBinding
    private val viewModel: CreateTeamViewModel by viewModels()
    private val sharedViewModel: CreateMatchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTeamBinding.inflate(inflater, container, false)

        setUpTeamPlayersRecyclerView()
        createTeam()
        addPlayer()
        return binding.root
    }

    private fun addPlayer() {
        binding.addPlayerBt.setOnClickListener {
            val destination = CreateTeamFragmentDirections.actionCreateTeamFragmentToPlayersFragment(true)
            findNavController().navigate(destination)
        }
    }

    private fun createTeam() {
        updatePlayers()
        binding.saveTeamBt.setOnClickListener {
            viewModel.saveTeam(getUserInput())
            Toast.makeText(requireContext(), "Team created", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePlayers(){
        viewModel.teamId.observe(viewLifecycleOwner){
            sharedViewModel.updateTeamOfPlayers(it)
            findNavController().navigateUp()
            sharedViewModel.teamPlayers.clear()
        }
    }

    private fun getUserInput() : Team {
        return Team(
            0L,
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
        recyclerView.adapter = PlayersShortAdapter(sharedViewModel.teamPlayers)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }


}