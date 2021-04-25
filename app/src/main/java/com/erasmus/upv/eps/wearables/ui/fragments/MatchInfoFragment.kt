package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentMatchInfoBinding
import com.erasmus.upv.eps.wearables.viewModels.MatchesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchInfoFragment : Fragment() {

    private lateinit var binding: FragmentMatchInfoBinding
    
    private val viewModel: MatchesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMatchInfoBinding.inflate(inflater, container, false)


        val args = MatchInfoFragmentArgs.fromBundle(requireArguments())
        viewModel.getInfoAboutTheMatch(args.matchId).observe(viewLifecycleOwner){
            binding.matchInfoTv.text = it.toString()
        }
        
        return binding.root
    }


}
