package com.erasmus.upv.eps.wearables.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.erasmus.upv.eps.wearables.R


class UpcomingMatchesFragment : Fragment() {

    companion object {
        fun newInstance() = UpcomingMatchesFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upcoming_matches, container, false)


        arguments?.takeIf { it.containsKey("TEST") }?.apply {
            view.findViewById<TextView>(R.id.textView5).text = getString("TEST")
        }

        view.findViewById<Button>(R.id.start_match_bt).setOnClickListener {
            buildAlertDialog().show()
        }

        return view
    }

    private fun buildAlertDialog(): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Do you want to start recording statistics for the match?")
            .setPositiveButton("Yes"
            ) { _, _ ->
                findNavController().navigate(R.id.action_matchesFragment_to_scanningBluetoothFragment)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }


}