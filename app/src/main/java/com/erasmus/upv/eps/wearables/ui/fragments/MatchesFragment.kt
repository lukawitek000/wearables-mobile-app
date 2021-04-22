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
        matchesAdapter = MatchesAdapter() {
            handleClickOnMatchesRecyclerView(it)
        }
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
