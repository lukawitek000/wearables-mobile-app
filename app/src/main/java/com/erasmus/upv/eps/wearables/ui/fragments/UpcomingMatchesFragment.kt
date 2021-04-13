package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.erasmus.upv.eps.wearables.R


class UpcomingMatchesFragment : Fragment() {

    companion object {
        fun newInstance() = UpcomingMatchesFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_upcoming_matches, container, false)


        arguments?.takeIf { it.containsKey("TEST") }?.apply {
            view.findViewById<TextView>(R.id.textView5).text = getString("TEST")
        }
        return view
    }


}