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
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentTeamsBinding
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
                              savedInstanceState: Bundle?): View? {

        binding = FragmentTeamsBinding.inflate(inflater, container, false)

        binding.createTeamFb.setOnClickListener {
            findNavController().navigate(R.id.action_teamsFragment_to_createTeamFragment)
        }

        setUpTeamsRecyclerView()
        loadDataFromDb()

        if(arguments != null){
            val args = TeamsFragmentArgs.fromBundle(requireArguments())
            Toast.makeText(requireContext() ,"args ${args.teamCreated}", Toast.LENGTH_SHORT).show()
            viewModel.teamCreated = args.teamCreated
        }

        return binding.root
    }

    private fun loadDataFromDb() {
        viewModel.getAllTeams().observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }

    private fun setUpTeamsRecyclerView() {
        val rv = binding.teamsRv
        adapter = TeamsAdapter(onClick = {
            if(viewModel.teamCreated == TeamCreated.NONE){
                return@TeamsAdapter
            }
            if(viewModel.teamCreated == TeamCreated.HOME_TEAM){
                sharedViewModel.homeTeam = it
                Toast.makeText(requireContext(), "Home team selected", Toast.LENGTH_SHORT).show()
            }else{
                sharedViewModel.guestTeam = it
                Toast.makeText(requireContext(), "Guest team selected", Toast.LENGTH_SHORT).show()
            }
            findNavController().navigateUp()
        }, onInfoClick ={
            val direction = TeamsFragmentDirections.actionTeamsFragmentToTeamInfoFragment(it.teamId)
            findNavController().navigate(direction)
        })
        rv.adapter = adapter
        rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

}