package com.erasmus.upv.eps.wearables.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.DialogFragmentSelectPlayerBinding
import com.erasmus.upv.eps.wearables.model.Player
import com.erasmus.upv.eps.wearables.ui.adapters.PlayersShortAdapter
import com.erasmus.upv.eps.wearables.viewModels.ReceivingDataViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectPlayerDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "SelectPlayerDialogFragment"
    }

    private lateinit var binding: DialogFragmentSelectPlayerBinding
    private val viewModel: ReceivingDataViewModel by hiltNavGraphViewModels(R.id.receiving_data_nested_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentSelectPlayerBinding.inflate(
                inflater, container, false
        )
        isCancelable = false
        setRegisteredActionText()
        handleNotChoosingPlayer()
        setUpPlayerRecyclerView()
        return binding.root
    }

    private fun setRegisteredActionText() {
        binding.registeredActionPlayerTv.text = getString(R.string.registered_action, viewModel.getLastActionRecorded())
    }

    private fun handleNotChoosingPlayer() {
        binding.closeSelectPlayerDialogIv.setOnClickListener {
            viewModel.dismissSelectingPlayer()
            dismiss()
        }
    }

    private fun setUpPlayerRecyclerView() {
        val rv = binding.selectPlayerRv
        rv.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        val adapter = PlayersShortAdapter(this::selectPlayer, isDeletable = false)
        adapter.submitList(viewModel.getPlayersFromChosenTeam())
        rv.adapter = adapter
    }

    private fun selectPlayer(player: Player){
        viewModel.selectPlayer(player.playerId)
        dismiss()
    }


}