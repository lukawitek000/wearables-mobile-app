package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.ui.dialogs.SelectPlayerDialogFragment
import com.erasmus.upv.eps.wearables.ui.dialogs.SelectTeamDialogFragment


class CurrentMatchFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view =  inflater.inflate(R.layout.fragment_current_match, container, false)

        view.findViewById<Button>(R.id.ask_team_bt).setOnClickListener {
            SelectTeamDialogFragment().show(childFragmentManager, SelectTeamDialogFragment.TAG)
        }


        view.findViewById<Button>(R.id.ask_player_bt).setOnClickListener {
            SelectPlayerDialogFragment().show(childFragmentManager, SelectPlayerDialogFragment.TAG)
        }

        return view
    }


}