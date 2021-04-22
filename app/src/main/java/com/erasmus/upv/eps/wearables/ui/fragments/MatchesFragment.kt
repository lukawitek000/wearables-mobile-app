package com.erasmus.upv.eps.wearables.ui.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentMatchesBinding
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.ui.adapters.MatchesAdapter
import java.time.LocalDate


class MatchesFragment : Fragment() {

    companion object {
        fun newInstance() = MatchesFragment()
        const val MATCH_TIME_KEY = "MATCH_TIME_KEY"
        enum class MatchTime {
            UPCOMING, PAST
        }
    }

    private lateinit var binding: FragmentMatchesBinding
    private lateinit var matchesAdapter: MatchesAdapter

    private var matchesType = MatchTime.UPCOMING



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchesBinding.inflate(inflater, container, false)

        arguments?.takeIf {
            it.containsKey(MATCH_TIME_KEY)
        }?.apply {
            matchesType = getSerializable(MATCH_TIME_KEY) as MatchTime
        }
        setUpMatchesRecyclerView()

        return binding.root
    }

    private fun setUpMatchesRecyclerView() {
        binding.matchesRv.layoutManager = LinearLayoutManager(requireContext())
        matchesAdapter = MatchesAdapter(getMatchesList()) {
            handleClickOnMatchesRecyclerView(it)
        }
        binding.matchesRv.adapter = matchesAdapter
    }

    private fun getMatchesList() = if(matchesType == MatchTime.UPCOMING){
        upcomingMatches
    }else{
        pastMatches
    }

    private fun handleClickOnMatchesRecyclerView(match: Match){
        if(matchesType ==  MatchTime.UPCOMING) {
            buildAlertDialog(match).show()
        }else{
            findNavController().navigate(R.id.action_matchesFragment_to_matchStatisticsFragment)
        }
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





val upcomingMatches = listOf<Match>(
//    Match(
//        id = "1",
//        guestTeam = Team("1", "LKS",
//            listOf(Player(1L, "Adam", "Football", 10, "GK")),
//            "Football", Color.CYAN, "Poland", "Lodz", null, "Other info"),
//        homeTeam = Team("2", "Widzew", listOf(Player(2L, "Piotr", "Football", 9, "CF")),
//            "Football", Color.BLUE, "Poland", "Lodz", null, "Other info 2"),
//        date = LocalDate.of(2021, 5, 12),
//        location = "Lodz",
//        sport = "Football",
//        league = "Ekstraklasa",
//        otherDetails = "Some details",
//    ),
//    Match(
//        id = "2",
//        guestTeam = Team("1", "LKS",
//            listOf(Player(1L, "Adam", "Football", 10, "GK")),
//            "Football", Color.CYAN, "Poland", "Lodz", null, "Other info"),
//        homeTeam = Team("2", "Widzew", listOf(Player(2L, "Piotr", "Football", 9, "CF")),
//            "Football", Color.BLUE, "Poland", "Lodz", null, "Other info 2"),
//        date = LocalDate.of(2021, 6, 2),
//        location = "Lodz",
//        sport = "Football",
//        league = "Ekstraklasa",
//        otherDetails = "Some details",
//    ), Match(
//        id = "3",
//        guestTeam = Team("1", "LKS",
//            listOf(Player(1L, "Adam", "Football", 10, "GK")),
//            "Football", Color.CYAN, "Poland", "Lodz", null, "Other info"),
//        homeTeam = Team("2", "Widzew", listOf(Player(2L, "Piotr", "Football", 9, "CF")),
//            "Football", Color.BLUE, "Poland", "Lodz", null, "Other info 2"),
//        date = LocalDate.of(2021, 10, 20),
//        location = "Lodz",
//        sport = "Football",
//        league = "Ekstraklasa",
//        otherDetails = "Some details",
//    )
)

private val pastMatches = listOf<Match>(
//    Match(
//        id = "10",
//        guestTeam = Team("1", "LKS",
//            listOf(Player(1L, "Adam", "Football", 10, "GK")),
//            "Football", Color.CYAN, "Poland", "Lodz", null, "Other info"),
//        homeTeam = Team("2", "Widzew", listOf(Player(2L, "Piotr", "Football", 9, "CF")),
//            "Football", Color.BLUE, "Poland", "Lodz", null, "Other info 2"),
//        date = LocalDate.of(2021, 1, 2),
//        location = "Lodz",
//        sport = "Football",
//        league = "Ekstraklasa",
//        otherDetails = "Some details",
//    ),
//    Match(
//        id = "20",
//        guestTeam = Team("1", "LKS",
//            listOf(Player(1L, "Adam", "Football", 10, "GK")),
//            "Football", Color.CYAN, "Poland", "Lodz", null, "Other info"),
//        homeTeam = Team("2", "Widzew", listOf(Player(2L, "Piotr", "Football", 9, "CF")),
//            "Football", Color.BLUE, "Poland", "Lodz", null, "Other info 2"),
//        date = LocalDate.of(2020, 1, 20),
//        location = "Lodz",
//        sport = "Football",
//        league = "Ekstraklasa",
//        otherDetails = "Some details",
//    ), Match(
//        id = "30",
//        guestTeam = Team("1", "LKS Past",
//            listOf(Player(1L, "Adam", "Football", 10, "GK")),
//            "Football", Color.CYAN, "Poland", "Lodz", null, "Other info"),
//        homeTeam = Team("2", "Widzew", listOf(Player(2L, "Piotr", "Football", 9, "CF")),
//            "Football", Color.BLUE, "Poland", "Lodz", null, "Other info 2"),
//        date = LocalDate.of(2021, 2, 11),
//        location = "Lodz",
//        sport = "Football",
//        league = "Ekstraklasa",
//        otherDetails = "Some details",
//    )
)