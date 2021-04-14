package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.erasmus.upv.eps.wearables.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class DialogWhichPlayerFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "DialogWhichPlayerFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog_which_player, container, false)
    }


}