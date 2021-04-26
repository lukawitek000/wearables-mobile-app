package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentPlayerInfoBinding
import com.erasmus.upv.eps.wearables.viewModels.PlayersViewModel
import dagger.hilt.android.AndroidEntryPoint

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
        if(arguments != null){
            val args = PlayerInfoFragmentArgs.fromBundle(requireArguments())
            if(args.playerId > 0L) {
                viewModel.playerId = args.playerId
                viewModel.getDetailsAboutPlayer(args.playerId).observe(viewLifecycleOwner) {
                    binding.playerInfoTv.text = it.toString()
                    viewModel.playerInfo = it
                    areButtonsEnable(true)
                }
            }else{
                findNavController().navigateUp()
            }
        }
        handleDeleteButton()
        handleUpdateButton()

        return binding.root
    }

    private fun handleUpdateButton() {
        binding.updatePlayerBt.setOnClickListener {
            val directions = PlayerInfoFragmentDirections.actionPlayerInfoFragmentToCreatePlayerFragment(viewModel.playerId)
            findNavController().navigate(directions)
        }
    }

    private fun areButtonsEnable(isEnabled: Boolean) {
        binding.deletePlayerBt.isEnabled = isEnabled
        binding.updatePlayerBt.isEnabled = isEnabled
    }

    private fun handleDeleteButton() {
        binding.deletePlayerBt.setOnClickListener {
            Log.i("PlayerInfoFragment", "handleDeleteButton: ${viewModel.playerInfo}")
            if(viewModel.playerInfo != null) {
                viewModel.deletePlayer(viewModel.playerInfo!!)
                findNavController().navigateUp()
            }
        }
    }


}