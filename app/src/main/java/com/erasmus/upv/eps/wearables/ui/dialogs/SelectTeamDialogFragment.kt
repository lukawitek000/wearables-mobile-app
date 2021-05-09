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
        binding.team1Bt.text = viewModel.homeTeam.team.name
        binding.team2Bt.text = viewModel.guestTeam.team.name
        binding.team1Bt.setOnClickListener {
            Toast.makeText(requireContext(), "${binding.team1Bt.text} selected", Toast.LENGTH_SHORT).show()
            Timber.d("Team 1 selected")
            viewModel.selectHomeTeam()
            dismiss()
        }
        binding.team2Bt.setOnClickListener {
            Toast.makeText(requireContext(), "${binding.team2Bt.text} selected", Toast.LENGTH_SHORT).show()
            Timber.d("Team 2 selected")
            viewModel.selectGuestTeam()
            dismiss()
        }

        return binding.root
    }


}