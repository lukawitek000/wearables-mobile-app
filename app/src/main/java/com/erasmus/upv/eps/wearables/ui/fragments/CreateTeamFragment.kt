package com.erasmus.upv.eps.wearables.ui.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentCreateTeamBinding
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.ui.adapters.PlayersShortAdapter
import com.erasmus.upv.eps.wearables.viewModels.CreateTeamViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateTeamFragment : Fragment() {


    private lateinit var binding: FragmentCreateTeamBinding
    private val viewModel: CreateTeamViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTeamBinding.inflate(inflater, container, false)

        setUpTeamPlayersRecycelerView()
        createTeam()

        return binding.root
    }

    private fun createTeam() {
        binding.saveTeamBt.setOnClickListener {
            viewModel.saveTeam(getUserInput())
            Toast.makeText(requireContext(), "Team saved", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
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

    private fun setUpTeamPlayersRecycelerView() {
        val recyclerView = binding.teamPlayersRv
        recyclerView.adapter = PlayersShortAdapter(players)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }


}

val players = listOf<Player>(
        Player(1L, "Adam", "Football", 1, "GK"),
        Player(2L, "Lukasz", "Football", 22, "GK"),
        Player(3L, "Lewandowski", "Football", 9, "CF"),
        Player(4L, "Robert", "Football", 11, "RM"),
        Player(5L, "Piotr", "Football", 89, "CM"),
        Player(6L, "Dennys", "Football", 99, "CB")
)