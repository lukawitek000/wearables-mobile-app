package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.erasmus.upv.eps.wearables.MainActivity
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentTeamsBinding
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.ui.adapters.TeamsAdapter
import com.erasmus.upv.eps.wearables.util.TeamCreated
import com.erasmus.upv.eps.wearables.viewModels.CreateRelationsViewModel
import com.erasmus.upv.eps.wearables.viewModels.TeamsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamsFragment : Fragment() {

    private lateinit var binding: FragmentTeamsBinding
    private lateinit var adapter: TeamsAdapter
    private val viewModel: TeamsViewModel by viewModels()
    private val sharedViewModel: CreateRelationsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentTeamsBinding.inflate(inflater, container, false)
        handleCreateTeamFb()
        setUpTeamsRecyclerView()
        loadDataFromDb()
        changeVisibilityOfBottomMenu()
        return binding.root
    }

    private fun handleCreateTeamFb() {
        binding.createTeamFb.setOnClickListener {
            findNavController().navigate(R.id.action_teamsFragment_to_createTeamFragment)
        }
    }

    private fun changeVisibilityOfBottomMenu() {
        if(sharedViewModel.whichTeamIsCreated == TeamCreated.NONE){
            (activity as MainActivity).setBottomNavigationVisibility(View.VISIBLE)
        }else{
            (activity as MainActivity).setBottomNavigationVisibility(View.GONE)
        }
    }

    private fun loadDataFromDb() {
        viewModel.getAllTeams().observe(viewLifecycleOwner){
            val teams = filterTeams(it)
            adapter.submitList(teams)
            if(teams.isEmpty()){
                binding.teamsRv.visibility = View.GONE
                binding.noTeamsInfoTv.visibility = View.VISIBLE
            }else{
                binding.teamsRv.visibility = View.VISIBLE
                binding.noTeamsInfoTv.visibility = View.GONE
            }
        }
    }

    private fun filterTeams(teams: List<Team>): List<Team> {
        return teams.filter {
            it.teamId != sharedViewModel.guestTeam?.teamId && it.teamId != sharedViewModel.homeTeam?.teamId
        }
    }

    private fun setUpTeamsRecyclerView() {
        val rv = binding.teamsRv
        adapter = TeamsAdapter(onClickTeamItem = this::onClickTeamItem, onClickTeamInfo = this::onClickTeamInfo)
        rv.adapter = adapter
        rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun onClickTeamItem(team: Team){
        if(sharedViewModel.whichTeamIsCreated == TeamCreated.NONE){
            return
        }
        createProperTeam(team)
        findNavController().navigateUp()
    }

    private fun createProperTeam(team: Team) {
        if (sharedViewModel.whichTeamIsCreated == TeamCreated.HOME_TEAM) {
            sharedViewModel.homeTeam = team
            Toast.makeText(requireContext(), "Home team selected", Toast.LENGTH_SHORT).show()
        } else {
            sharedViewModel.guestTeam = team
            Toast.makeText(requireContext(), "Guest team selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClickTeamInfo(team: Team){
        val direction = TeamsFragmentDirections.actionTeamsFragmentToTeamInfoFragment(team.teamId)
        findNavController().navigate(direction)
    }


}