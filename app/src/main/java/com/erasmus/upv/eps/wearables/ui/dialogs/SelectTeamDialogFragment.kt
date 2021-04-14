package com.erasmus.upv.eps.wearables.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.erasmus.upv.eps.wearables.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SelectTeamDialogFragment : BottomSheetDialogFragment() {

    companion object{
        const val TAG = "SelectTeamDialogFragment"
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_fragment_select_team, container, false)
    }


}