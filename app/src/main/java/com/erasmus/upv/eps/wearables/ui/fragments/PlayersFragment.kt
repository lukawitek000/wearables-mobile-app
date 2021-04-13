package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.erasmus.upv.eps.wearables.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class PlayersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_players, container, false)

        view.findViewById<FloatingActionButton>(R.id.create_player_fb).setOnClickListener {
            findNavController().navigate(R.id.action_playersFragment_to_createPlayerFragment)
        }

        return view
    }


}