package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentCreateTeamBinding
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.ui.adapters.PlayersAdapter


class CreateTeamFragment : Fragment() {


    private lateinit var binding: FragmentCreateTeamBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTeamBinding.inflate(inflater, container, false)

        val recyclerView = binding.teamPlayersRv
        recyclerView.adapter = PlayersAdapter(players)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        return binding.root
    }


}

val players = listOf<Player>(
        Player("1", "Adam", "Football", 1, "GK"),
        Player("2", "Lukasz", "Football", 22, "GK"),
        Player("3", "Lewandowski", "Football", 9, "CF"),
        Player("4", "Robert", "Football", 11, "RM"),
        Player("5", "Piotr", "Football", 89, "CM"),
        Player("6", "Dennys", "Football", 99, "CB")
)