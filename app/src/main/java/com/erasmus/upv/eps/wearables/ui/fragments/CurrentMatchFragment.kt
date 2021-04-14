package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.erasmus.upv.eps.wearables.R


class CurrentMatchFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view =  inflater.inflate(R.layout.fragment_current_match, container, false)

        view.findViewById<Button>(R.id.ask_team_bt).setOnClickListener {
            DialogWhichTeamFragment().show(childFragmentManager, DialogWhichTeamFragment.TAG)
        }


        view.findViewById<Button>(R.id.ask_player_bt).setOnClickListener {
            DialogWhichPlayerFragment().show(childFragmentManager, DialogWhichPlayerFragment.TAG)
        }

        return view
    }


}