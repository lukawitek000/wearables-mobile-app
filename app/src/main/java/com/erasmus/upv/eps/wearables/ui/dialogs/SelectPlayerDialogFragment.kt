package com.erasmus.upv.eps.wearables.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.DialogFragmentSelectPlayerBinding
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.ui.adapters.PlayersShortAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SelectPlayerDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "SelectPlayerDialogFragment"
    }

    private lateinit var binding: DialogFragmentSelectPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentSelectPlayerBinding.inflate(
                inflater, container, false
        )

        val rv = binding.selectPlayerRv
        rv.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        rv.adapter = PlayersShortAdapter(players){
            Toast.makeText(requireContext(), "Selected ${it.name} player", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        return binding.root
    }


}

private val players = listOf<Player>(
        Player(
                "1", "Luke", "Football", 1, "Gk"
        ),
        Player(
                "1", "Piotr", "Football", 12, "Gk"
        ),
        Player(
                "1", "qweqwe", "Football", 120, "Gk"
        ),
        Player(
                "1", "ertg", "Football", 1200, "Gk"
        ),
        Player(
                "1", "dsfg", "Football", 12000, "Gk"
        ),
        Player(
                "1", "sdhf", "Football", 120000, "Gk"
        ),
        Player(
                "1", "dghs", "Football", 1200000, "Gk"
        ),
        Player(
                "1", "xcv", "Football", 12000000, "Gk"
        ),
)