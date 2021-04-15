package com.erasmus.upv.eps.wearables.ui.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentTeamsBinding
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.ui.adapters.TeamsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TeamsFragment : Fragment() {

    private lateinit var binding: FragmentTeamsBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentTeamsBinding.inflate(inflater, container, false)

        binding.createTeamFb.setOnClickListener {
            findNavController().navigate(R.id.action_teamsFragment_to_createTeamFragment)
        }

        val rv = binding.teamsRv
        rv.adapter = TeamsAdapter(teams)
        rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        return binding.root
    }

}


val teams = listOf<Team>(
        Team("2", "Widzew", listOf(Player("2", "Piotr", "Football", 9, "CF")),
                "Football", Color.BLUE, "Poland", "Lodz", null, "Other info 2"),
        Team("2", "LKS", listOf(Player("2", "Piotr", "Football", 9, "CF")),
                "Football", Color.BLUE, "Poland", "Lodz", null, "Other info 2"),
        Team("2", "Levante", listOf(Player("2", "Piotr", "Football", 9, "CF")),
                "Football", Color.BLUE, "Poland", "Valencia", null, "Other info 2"),
        Team("2", "Real Madrid", listOf(Player("2", "Piotr", "Football", 9, "CF")),
                "Football", Color.BLUE, "Poland", "Madrid", null, "Other info 2"),
        Team("2", "Chelsea", listOf(Player("2", "Piotr", "Football", 9, "CF")),
                "Football", Color.BLUE, "Poland", "London", null, "Other info 2"),
        Team("2", "Lech Poznan", listOf(Player("2", "Piotr", "Football", 9, "CF")),
                "Football", Color.BLUE, "Poland", "Poznan", null, "Other info 2"),
        Team("2", "PSG", listOf(Player("2", "Piotr", "Football", 9, "CF")),
                "Football", Color.BLUE, "Poland", "Paris", null, "Other info 2")
)