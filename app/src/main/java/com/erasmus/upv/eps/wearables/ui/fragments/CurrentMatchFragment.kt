package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentCurrentMatchBinding
import com.erasmus.upv.eps.wearables.model.LiveAction
import com.erasmus.upv.eps.wearables.model.Response
import com.erasmus.upv.eps.wearables.ui.adapters.LiveActionsAdapter
import com.erasmus.upv.eps.wearables.ui.adapters.ResponseDataAdapter
import com.erasmus.upv.eps.wearables.ui.dialogs.SelectPlayerDialogFragment
import com.erasmus.upv.eps.wearables.ui.dialogs.SelectTeamDialogFragment


class CurrentMatchFragment : Fragment() {

    private lateinit var binding: FragmentCurrentMatchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentCurrentMatchBinding.inflate(
                inflater, container, false
        )
        binding.simulateWhichTeamBn.setOnClickListener {
            SelectTeamDialogFragment().show(childFragmentManager, SelectTeamDialogFragment.TAG)
        }


        binding.simulateWhichPlayerBn.setOnClickListener {
            SelectPlayerDialogFragment().show(childFragmentManager, SelectPlayerDialogFragment.TAG)
        }

        val rv = binding.liveActionsRv
        rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv.adapter = LiveActionsAdapter(liveActions)


        return binding.root
    }


}


private val liveActions = listOf<LiveAction>(
        LiveAction("1:11", "Goal", "none"),
        LiveAction("21:11", "Foul", "none"),
        LiveAction("1:22", "Offside", "sdfds"),
        LiveAction("9:51", "Goal", "none"),
        LiveAction("7:00", "Assist", "gdg"),
        LiveAction("1:44", "Foul", "none"),
        LiveAction("12:11", "Goal", "dfgdf"),
        LiveAction("90:11", "Offside", "none"),
        LiveAction("13:11", "Save", "none"),
        LiveAction("51:11", "Assist", "egeg"),


)