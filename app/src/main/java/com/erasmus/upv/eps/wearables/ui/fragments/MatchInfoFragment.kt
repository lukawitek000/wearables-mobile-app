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
import com.erasmus.upv.eps.wearables.databinding.FragmentMatchInfoBinding
import com.erasmus.upv.eps.wearables.viewModels.MatchesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchInfoFragment : Fragment() {

    private lateinit var binding: FragmentMatchInfoBinding
    
    private val viewModel: MatchesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentMatchInfoBinding.inflate(inflater, container, false)

        handleNoArgumentsPassedToFragment()
        receiveSafeArgs()
        setMatchInfoTextView()
        handleDeleteMatchButton()
        handleUpdateMatchButton()
        
        return binding.root
    }


    private fun setMatchInfoTextView() {
        viewModel.getInfoAboutTheMatch().observe(viewLifecycleOwner) {
            binding.matchInfoTv.text = it.toString()
        }
    }

    private fun receiveSafeArgs() {
        val args = MatchInfoFragmentArgs.fromBundle(requireArguments())
        viewModel.matchId = args.matchId
    }

    private fun handleNoArgumentsPassedToFragment() {
        if (arguments == null) {
            findNavController().navigateUp()
        }
    }

    private fun handleDeleteMatchButton() {
        binding.deleteMatchBt.setOnClickListener {
            deleteMatch()
        }
    }

    private fun deleteMatch() {
        viewModel.deleteMatch()
        Toast.makeText(requireContext(), "Match deleted", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }


    private fun handleUpdateMatchButton() {
        binding.updateMatchBt.setOnClickListener {
            val direction = MatchInfoFragmentDirections.actionMatchInfoFragmentToCreateMatchFragment(viewModel.matchId)
            findNavController().navigate(direction)
        }
    }


}
