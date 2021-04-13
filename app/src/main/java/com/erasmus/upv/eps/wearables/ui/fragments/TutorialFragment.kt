package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.erasmus.upv.eps.wearables.R


class TutorialFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.tutorial_fragment, container, false)
        view.findViewById<Button>(R.id.button).setOnClickListener {
            findNavController().navigate(R.id.action_tutorialFragment_to_matchesFragment)
        }
        return view
    }


}