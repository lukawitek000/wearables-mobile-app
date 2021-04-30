package com.erasmus.upv.eps.wearables.ui.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.erasmus.upv.eps.wearables.databinding.FragmentTeamInfoBinding
import com.erasmus.upv.eps.wearables.viewModels.TeamsViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.Exception

@AndroidEntryPoint
class TeamInfoFragment : Fragment() {

    private val viewModel: TeamsViewModel by viewModels()

    private lateinit var binding: FragmentTeamInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentTeamInfoBinding.inflate(inflater, container, false)
        areButtonsEnabled(false)
        receiveSafeArgs()
        handleDeleteButton()
        handleUpdateButton()
        return binding.root
    }

    private fun areButtonsEnabled(isEnabled: Boolean) {
        binding.deleteTeamBt.isEnabled = isEnabled
        binding.updateTeamBt.isEnabled = isEnabled
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
            viewModel.isTeamDeleted.observe(viewLifecycleOwner){
                if(it){
                    findNavController()
                }else{
                    showDialogInformingThatTeamIsPartOfMatch()
                }
            }
        }
    }

    private fun showDialogInformingThatTeamIsPartOfMatch() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Cannot delete the team")
        builder.setMessage("The team is part of a match, so it cannot be deleted")
        builder.setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }

    private fun receiveSafeArgs() {
        if (arguments != null) {
            val args = TeamInfoFragmentArgs.fromBundle(requireArguments())
            viewModel.teamId = args.teamId
            setTextToTeamDetailsRawData(args.teamId)
        }
    }

    private fun setTextToTeamDetailsRawData(teamId: Long) {
        viewModel.getTeamDetailInfo(teamId).observe(viewLifecycleOwner) {
            try {
                areButtonsEnabled(true)
                binding.teamInfoTv.text = it.toString()
            }catch (e: Exception) {
                Timber.e("No team in the database")
                findNavController().navigateUp()
            }
        }
    }


}