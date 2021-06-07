package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentMatchStatisticsBinding
import com.erasmus.upv.eps.wearables.model.LiveAction
import com.erasmus.upv.eps.wearables.ui.adapters.ActionsAdapter
import com.erasmus.upv.eps.wearables.ui.adapters.LiveActionsAdapter
import com.erasmus.upv.eps.wearables.viewModels.MatchStatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MatchStatisticsFragment : Fragment() {

    private lateinit var binding: FragmentMatchStatisticsBinding
    private val viewModel: MatchStatisticsViewModel by viewModels()
    private lateinit var adapter: LiveActionsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchStatisticsBinding.inflate(inflater, container, false)


        setUpRecyclerView()
        receiveSafeArgs()
        getMatchWithTeams()
        getLiveActions()
        observeScore()

        return binding.root
    }

    private fun observeScore() {
        viewModel.homeTeamScore.observe(viewLifecycleOwner){
            binding.homeTeamScoreTv.text = it.toString()
        }
        viewModel.guestTeamScore.observe(viewLifecycleOwner){
            binding.guestTeamScoreTv.text = it.toString()
        }
    }


    private fun setTeamNames() {
        binding.homeTeamNameTv.text = viewModel.homeTeam.team.name
        binding.guestTeamNameTv.text = viewModel.guestTeam.team.name
    }

    private fun getMatchWithTeams() {
        viewModel.getMatchWithTeams().observe(viewLifecycleOwner){
            Timber.d("Match with teams $it")
            viewModel.setMatchAndTeams(it)
            viewModel.getTeamWithPlayers(it.teams)
        }
        viewModel.teamsWithPlayers.observe(viewLifecycleOwner){
            Timber.d("TeamWithPLayers $it")
            viewModel.setTeamsAndPlayers(it)
            setTeamNames()
            getLiveActions()
        }
    }

    private fun receiveSafeArgs() {
        if(arguments != null){
            val args = MatchStatisticsFragmentArgs.fromBundle(requireArguments())
            viewModel.matchId = args.matchId
            if(viewModel.matchId == 0L){
                findNavController().navigateUp()
            }
        }
    }

    private fun setUpRecyclerView() {
        val rv = binding.liveActionsRv
        rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = LiveActionsAdapter(
            viewModel::deleteLiveAction,
            viewModel::getTeamColor,
            viewModel::getPlayerNumberById,
            viewModel::getTeamNameById
        )
        rv.adapter = adapter
    }



    private fun getLiveActions() {
        viewModel.getMatchStatistics(viewModel.matchId).observe(viewLifecycleOwner){
            viewModel.calculateScore(it)
            adapter.submitList(it.toMutableList())
        }
    }


}