package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.erasmus.upv.eps.wearables.databinding.FragmentPlayerInfoBinding
import com.erasmus.upv.eps.wearables.viewModels.PlayersViewModel
import dagger.hilt.android.AndroidEntryPoint


//TODO show info in edit/update screen ?? but how

@AndroidEntryPoint
class PlayerInfoFragment : Fragment() {

    private lateinit var binding: FragmentPlayerInfoBinding
    private val viewModel: PlayersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerInfoBinding.inflate(inflater, container, false)
        areButtonsEnable(false)
        receiveSafeArgs()
        handleDeleteButton()
        handleUpdateButton()
        return binding.root
    }


    private fun receiveSafeArgs() {
        if (arguments != null) {
            val args = PlayerInfoFragmentArgs.fromBundle(requireArguments())
            if (args.playerId > 0L) {
                getDetailsAboutPlayer(args.playerId)
            }
        }
    }


    private fun getDetailsAboutPlayer(playerId: Long) {
        viewModel.selectedPlayer.playerId = playerId
        viewModel.getDetailsAboutPlayer(playerId).observe(viewLifecycleOwner) {
            binding.playerInfoTv.text = it.toString()
            viewModel.selectedPlayer = it
            areButtonsEnable(true)
        }
    }


    private fun handleUpdateButton() {
        binding.updatePlayerBt.setOnClickListener {
            val directions = PlayerInfoFragmentDirections.actionPlayerInfoFragmentToCreatePlayerFragment(viewModel.selectedPlayer.playerId)
            findNavController().navigate(directions)
        }
    }


    private fun areButtonsEnable(isEnabled: Boolean) {
        binding.deletePlayerBt.isEnabled = isEnabled
        binding.updatePlayerBt.isEnabled = isEnabled
    }


    private fun handleDeleteButton() {
        binding.deletePlayerBt.setOnClickListener {
            viewModel.deletePlayer(viewModel.selectedPlayer)
            findNavController().navigateUp()
            Toast.makeText(requireContext(), "Player deleted", Toast.LENGTH_SHORT).show()
        }
    }

}