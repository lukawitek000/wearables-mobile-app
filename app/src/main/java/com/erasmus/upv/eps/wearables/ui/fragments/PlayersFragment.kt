package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentPlayersBinding
import com.erasmus.upv.eps.wearables.ui.adapters.PlayersAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class PlayersFragment : Fragment() {

    private lateinit var binding: FragmentPlayersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayersBinding.inflate(inflater, container, false)

        binding.createPlayerFb.setOnClickListener {
            findNavController().navigate(R.id.action_playersFragment_to_createPlayerFragment)
        }

        val rv = binding.playersRv
        rv.adapter = PlayersAdapter(players)
        rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        return binding.root
    }


}