package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.erasmus.upv.eps.wearables.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TeamsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view =  inflater.inflate(R.layout.fragment_teams, container, false)


        view.findViewById<FloatingActionButton>(R.id.create_team_fb).setOnClickListener {
            findNavController().navigate(R.id.action_teamsFragment_to_createTeamFragment)
        }

        return view
    }

}