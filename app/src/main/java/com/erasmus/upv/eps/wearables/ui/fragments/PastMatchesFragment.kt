package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.erasmus.upv.eps.wearables.R


class PastMatchesFragment : Fragment() {

    companion object{
        fun newInstance() = PastMatchesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_past_matches, container, false)

        view.findViewById<Button>(R.id.select_some_match_bt).setOnClickListener {
            findNavController().navigate(R.id.action_matchesFragment_to_matchStatisticsFragment)
        }

        return view
    }


}