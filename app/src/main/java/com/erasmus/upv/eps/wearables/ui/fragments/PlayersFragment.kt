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
import com.erasmus.upv.eps.wearables.MainActivity
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentPlayersBinding
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.ui.adapters.PlayersAdapter
import com.erasmus.upv.eps.wearables.util.DialogBuilder
import com.erasmus.upv.eps.wearables.viewModels.CreateRelationsViewModel
import com.erasmus.upv.eps.wearables.viewModels.PlayersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayersFragment : Fragment() {

    private lateinit var binding: FragmentPlayersBinding
    private val viewModel: PlayersViewModel by viewModels()
    private lateinit var playersAdapter: PlayersAdapter
    private val sharedViewModel: CreateRelationsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayersBinding.inflate(inflater, container, false)
        receiveSafeArgs()
        handleCreatePlayerFb()
        changeVisibilityOfBottomMenu()
        setUpRecyclerView()
        listenToDbChanges()
        return binding.root
    }

    private fun receiveSafeArgs() {
        if (arguments != null) {
            val args = PlayersFragmentArgs.fromBundle(requireArguments())
            sharedViewModel.isCreatingTeam = args.isCreatingTeam
            if(sharedViewModel.isCreatingTeam){
                (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun handleCreatePlayerFb() {
        binding.createPlayerFb.setOnClickListener {
            findNavController().navigate(R.id.action_playersFragment_to_createPlayerFragment)
        }
    }


    private fun changeVisibilityOfBottomMenu() {
        if(sharedViewModel.isCreatingTeam){
            (activity as MainActivity).setBottomNavigationVisibility(View.GONE)
        }else{
            (activity as MainActivity).setBottomNavigationVisibility(View.VISIBLE)
        }
    }


    private fun setUpRecyclerView() {
        val rv = binding.playersRv
        playersAdapter = PlayersAdapter(
                onClickPlayerItem = this::onClickPlayerItem
        )
        rv.adapter = playersAdapter
        rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun onClickPlayerItem(player: Player){
        if(sharedViewModel.isCreatingTeam) {
            addPlayerToTheTeam(player)
        }else{
            //navigateToPlayerInfoFragment(player)
            showDialogWithPlayerOptions(player)
        }
    }

    private fun showDialogWithPlayerOptions(player: Player) {
        DialogBuilder.buildAndShowDialog(
            requireContext(),
            requireActivity().layoutInflater,
            "Player action",
            "What do you want to do with this player?",
            yesButtonText = "Delete",
            noButtonText = "Update",
            yesButtonAction = { dialog ->
                viewModel.deletePlayer(player)
                dialog.dismiss()
            },
            noButtonAction = { dialog ->
                navigateToUpdateThePlayer(player.playerId)
                dialog.dismiss()
            }
        )
    }

    private fun navigateToUpdateThePlayer(playerId: Long) {
        val directions = PlayersFragmentDirections.actionPlayersFragmentToCreatePlayerFragment(playerId = playerId)
        findNavController().navigate(directions)
    }


    private fun addPlayerToTheTeam(player: Player){
        sharedViewModel.teamPlayers.add(player)
        findNavController().navigateUp()
    }


    private fun navigateToPlayerInfoFragment(player: Player) {
        val directions = PlayersFragmentDirections.actionPlayersFragmentToPlayerInfoFragment(player.playerId)
        findNavController().navigate(directions)
    }



    private fun listenToDbChanges() {
        viewModel.getAllPlayers().observe(viewLifecycleOwner){
            val playersList = filterPlayers(it)
            playersAdapter.submitList(
                playersList
            )
            if(playersList.isEmpty()){
                binding.playersRv.visibility = View.GONE
                binding.noPlayersInfoTv.visibility = View.VISIBLE
            }else{
                binding.playersRv.visibility = View.VISIBLE
                binding.noPlayersInfoTv.visibility = View.GONE
            }
        }
    }

    private fun filterPlayers(players: List<Player>): List<Player>{
        return if(sharedViewModel.isCreatingTeam) {
            viewModel.filterPlayers(players, sharedViewModel.teamPlayers,
                sharedViewModel.creatingTeam.teamId, sharedViewModel.creatingTeam.sport)
        } else {
            players
        }
    }

}
