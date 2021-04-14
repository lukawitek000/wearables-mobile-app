package com.erasmus.upv.eps.wearables.ui.fragments

import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentUpcomingMatchesBinding
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.ui.adapters.MatchesAdapter
import java.time.LocalDate


class UpcomingMatchesFragment : Fragment() {

    companion object {
        fun newInstance() = UpcomingMatchesFragment()
    }

    private lateinit var binding: FragmentUpcomingMatchesBinding
    private lateinit var matchesAdapter: MatchesAdapter


    private val matches = listOf(
        Match(
            id = "1",
            guestTeam = Team("1", "LKS",
                listOf(Player("1", "Adam", "Football", 10, "GK")),
             "Football", Color.CYAN, "Poland", "Lodz", null, "Other info"),
            homeTeam = Team("2", "Widzew", listOf(Player("2", "Piotr", "Football", 9, "CF")),
            "Football", Color.BLUE, "Poland", "Lodz", null, "Other info 2"),
            date = LocalDate.now(),
            location = "Lodz",
            sport = "Football",
            league = "Ekstraklasa",
            otherDetails = "Some details",
        ),
        Match(
            id = "2",
            guestTeam = Team("1", "LKS",
                listOf(Player("1", "Adam", "Football", 10, "GK")),
                "Football", Color.CYAN, "Poland", "Lodz", null, "Other info"),
            homeTeam = Team("2", "Widzew", listOf(Player("2", "Piotr", "Football", 9, "CF")),
                "Football", Color.BLUE, "Poland", "Lodz", null, "Other info 2"),
            date = LocalDate.now(),
            location = "Lodz",
            sport = "Football",
            league = "Ekstraklasa",
            otherDetails = "Some details",
        ), Match(
            id = "3",
            guestTeam = Team("1", "LKS",
                listOf(Player("1", "Adam", "Football", 10, "GK")),
                "Football", Color.CYAN, "Poland", "Lodz", null, "Other info"),
            homeTeam = Team("2", "Widzew", listOf(Player("2", "Piotr", "Football", 9, "CF")),
                "Football", Color.BLUE, "Poland", "Lodz", null, "Other info 2"),
            date = LocalDate.now(),
            location = "Lodz",
            sport = "Football",
            league = "Ekstraklasa",
            otherDetails = "Some details",
        )
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val view = inflater.inflate(R.layout.fragment_upcoming_matches, container, false)
        binding = FragmentUpcomingMatchesBinding.inflate(inflater, container, false)

        setUpMatchesRecyclerView()

        return binding.root
    }

    private fun setUpMatchesRecyclerView() {
        binding.matchesRv.layoutManager = LinearLayoutManager(requireContext())
        matchesAdapter = MatchesAdapter(matches) {
            handleClickOnMatchesRecyclerView(it)
        }
        binding.matchesRv.adapter = matchesAdapter
    }

    private fun handleClickOnMatchesRecyclerView(match: Match){
        buildAlertDialog(match).show()
    }



    private fun buildAlertDialog(match: Match): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Do you want to start recording statistics for the match ${match.id}?")
            .setPositiveButton("Yes"
            ) { _, _ ->
                findNavController().navigate(R.id.action_matchesFragment_to_scanningBluetoothFragment)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }


}