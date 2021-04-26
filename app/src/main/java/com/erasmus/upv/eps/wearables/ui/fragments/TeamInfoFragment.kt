package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.erasmus.upv.eps.wearables.databinding.FragmentTeamInfoBinding
import com.erasmus.upv.eps.wearables.viewModels.TeamsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamInfoFragment : Fragment() {

    private val viewModel: TeamsViewModel by viewModels()

    private lateinit var binding: FragmentTeamInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentTeamInfoBinding.inflate(inflater, container, false)

        receiveSafeArgs()
        handleDeleteButton()
        binding.deleteTeamBt.visibility = View.GONE // TODO handle match effected by deleting team

        handleUpdateButton()
        return binding.root
    }

    private fun handleUpdateButton() {
        binding.updateTeamBt.setOnClickListener {
            val directions = TeamInfoFragmentDirections.actionTeamInfoFragmentToCreateTeamFragment(viewModel.teamId)
            findNavController().navigate(directions)
        }
    }

    private fun handleDeleteButton() {
        binding.deleteTeamBt.setOnClickListener {
            viewModel.deleteTeamById(viewModel.teamId)
            findNavController().navigateUp()
        }
    }

    private fun receiveSafeArgs() {
        if (arguments != null) {
            val args = TeamInfoFragmentArgs.fromBundle(requireArguments())
            viewModel.teamId = args.teamId
            setTextToTeamDetailsRawData(args)
        }
    }

    private fun setTextToTeamDetailsRawData(args: TeamInfoFragmentArgs) {
        viewModel.getTeamDetailInfo(args.teamId).observe(viewLifecycleOwner) {
            binding.teamInfoTv.text = it.toString()
        }
    }


}