package com.erasmus.upv.eps.wearables.ui.dialogs

import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.DialogFragmentSelectTeamBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SelectTeamDialogFragment : BottomSheetDialogFragment() {

    companion object{
        const val TAG = "SelectTeamDialogFragment"
    }


    private lateinit var binding: DialogFragmentSelectTeamBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentSelectTeamBinding.inflate(inflater, container, false)

        binding.team1Bt.setOnClickListener {
            Toast.makeText(requireContext(), "${binding.team1Bt.text} selected", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        binding.team2Bt.setOnClickListener {
            Toast.makeText(requireContext(), "${binding.team2Bt.text} selected", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        return binding.root
    }


}