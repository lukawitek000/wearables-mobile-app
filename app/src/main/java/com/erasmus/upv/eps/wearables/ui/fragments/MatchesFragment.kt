package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentMatchesBinding
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.ui.adapters.MatchesAdapter
import com.erasmus.upv.eps.wearables.util.Sports
import com.erasmus.upv.eps.wearables.viewModels.MatchesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

    private val viewModel: MatchesViewModel by viewModels()



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
        loadDataFromDb()
        setFilterObserver()
        return binding.root
    }

    private fun setFilterObserver() {
        binding.radioButtonLayout.sportRadioGroup.check(R.id.football_radio_button)
        binding.radioButtonLayout.sportRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.football_radio_button -> viewModel.filterSport = Sports.FOOTBALL
                R.id.handball_radio_button -> viewModel.filterSport = Sports.HANDBALL
                else -> viewModel.filterSport = Sports.BASKETBALL
            }
            loadDataFromDb()
        }
    }

    private fun loadDataFromDb() {
        if(matchesType == MatchTime.UPCOMING) {
            viewModel.getAllUpcomingMatches().observe(viewLifecycleOwner){
                showPlayersOrNoPlayersText(filterMatches(it))
            }
        }else{
            viewModel.getAllUpcomingMatches().observe(viewLifecycleOwner){
                showPlayersOrNoPlayersText(filterMatches(it))
            }
        }
    }

    private fun showPlayersOrNoPlayersText(
        matches: MutableList<Match>
    ) {
        if (matches.isEmpty()) {
            binding.noPlayersInfoTv.visibility = View.VISIBLE
            binding.matchesRv.visibility = View.INVISIBLE
        } else {
            binding.noPlayersInfoTv.visibility = View.INVISIBLE
            binding.matchesRv.visibility = View.VISIBLE
        }
        matchesAdapter.submitList(matches)
    }

    private fun filterMatches(matches: List<Match>?): MutableList<Match> {
        return matches?.filter {
            match -> match.sport == viewModel.filterSport
        }?.toMutableList() ?: mutableListOf()
    }

    private fun setUpMatchesRecyclerView() {
        binding.matchesRv.layoutManager = LinearLayoutManager(requireContext())
        matchesAdapter = MatchesAdapter( {
            handleClickOnMatchesRecyclerView(it)
        }, {
            Toast.makeText(requireContext(), "Show match info", Toast.LENGTH_SHORT).show()
            val destination = MatchesViewPagerFragmentDirections.actionMatchesFragmentToMatchInfoFragment(matchId = it.matchId)
            findNavController().navigate(destination)
        })
        binding.matchesRv.adapter = matchesAdapter
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
            .setTitle("Do you want to start recording statistics for the match ${match.matchId}?")
            .setPositiveButton("Yes"
            ) { _, _ ->
                navigateToScanningForBLEDevices(match.matchId)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

    private fun navigateToScanningForBLEDevices(matchId: Long) {
        val directions = MatchesViewPagerFragmentDirections.actionMatchesFragmentToReceivingDataNestedGraph(matchId)
        //findNavController().navigate(R.id.action_matchesFragment_to_scanningBluetoothFragment)
        findNavController().navigate(directions)
    }


}
