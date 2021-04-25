package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentPlayersBinding
import com.erasmus.upv.eps.wearables.ui.adapters.PlayersAdapter
import com.erasmus.upv.eps.wearables.viewModels.CreateMatchViewModel
import com.erasmus.upv.eps.wearables.viewModels.PlayersViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayersFragment : Fragment() {

    private lateinit var binding: FragmentPlayersBinding
    private val viewModel: PlayersViewModel by viewModels()
    private lateinit var playersAdapter: PlayersAdapter

    private val sharedViewModel: CreateMatchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayersBinding.inflate(inflater, container, false)

        binding.createPlayerFb.setOnClickListener {
            findNavController().navigate(R.id.action_playersFragment_to_createPlayerFragment)
        }

        if(arguments != null){
            val args = PlayersFragmentArgs.fromBundle(requireArguments())
            sharedViewModel.isCreatingTeam = args.isCreatingTeam
        }

        setUpRecyclerView()
        listenToDbChanges()

        return binding.root
    }

    private fun listenToDbChanges() {
        viewModel.getAllPlayers().observe(viewLifecycleOwner){
            playersAdapter.submitList(it)
        }
    }


    private fun setUpRecyclerView() {
        val rv = binding.playersRv
        playersAdapter = PlayersAdapter(){
            if(sharedViewModel.isCreatingTeam) {
                sharedViewModel.teamPlayers.add(it)
                findNavController().navigateUp()
            }
        }
        rv.adapter = playersAdapter
        rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }


}