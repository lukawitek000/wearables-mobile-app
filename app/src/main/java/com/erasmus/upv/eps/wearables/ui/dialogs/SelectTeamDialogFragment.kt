package com.erasmus.upv.eps.wearables.ui.dialogs

import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.DialogFragmentSelectTeamBinding
import com.erasmus.upv.eps.wearables.viewModels.ReceivingDataViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SelectTeamDialogFragment : BottomSheetDialogFragment() {

    companion object{
        const val TAG = "SelectTeamDialogFragment"
    }


    private lateinit var binding: DialogFragmentSelectTeamBinding
    private val viewModel: ReceivingDataViewModel by hiltNavGraphViewModels(R.id.receiving_data_nested_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentSelectTeamBinding.inflate(inflater, container, false)
        isCancelable = false
        setTeamNamesTexts()
        setRegisteredActionText()
        setHomeTeamButtonListener()
        setGuestTeamButtonListener()

        return binding.root
    }

    private fun setRegisteredActionText() {
        binding.registerActionTitleTv.text = getString(R.string.registered_action, viewModel.getLastActionRecorded())
    }

    private fun setGuestTeamButtonListener() {
        binding.team2Bt.setOnClickListener {
            viewModel.selectGuestTeam()
            dismiss()
        }
    }

    private fun setHomeTeamButtonListener() {
        binding.team1Bt.setOnClickListener {
            viewModel.selectHomeTeam()
            dismiss()
        }
    }

    private fun setTeamNamesTexts() {
        binding.team1Bt.text = viewModel.homeTeam.team.name
        binding.team2Bt.text = viewModel.guestTeam.team.name
    }


}