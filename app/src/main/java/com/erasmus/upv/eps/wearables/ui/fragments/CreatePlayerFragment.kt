package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentCreatePlayerBinding
import com.erasmus.upv.eps.wearables.viewModels.CreatePlayerViewModel


class CreatePlayerFragment : Fragment() {


    private lateinit var binding: FragmentCreatePlayerBinding

    private val viewModel: CreatePlayerViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePlayerBinding.inflate(
                inflater, container, false
        )

        getUserInput()


        return binding.root
    }

    private fun getUserInput() {
        getRadioButtonInput()
        setSavePlayerListener()
    }


    private fun getRadioButtonInput(){
        setPlayersSportBaseOnRadioButtonChecked(binding.sportRadioGroup.checkedRadioButtonId)
        binding.sportRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            setPlayersSportBaseOnRadioButtonChecked(checkedId)
        }
    }

    private fun setPlayersSportBaseOnRadioButtonChecked(checkedId: Int) {
        when (checkedId) {
            R.id.football_radio_button -> viewModel.setPlayersSport("Football")
            R.id.handball_radio_button -> viewModel.setPlayersSport("Handball")
            R.id.basketball_radio_button -> viewModel.setPlayersSport("Basketball")
        }
    }

    private fun setSavePlayerListener(){
        binding.savePlayerBt.setOnClickListener {
            viewModel.createPlayer(
                    binding.playerNameEt.text.toString(),
                    getPlayerNumber(),
                    binding.playerPositionEt.text.toString(),
                    binding.playerOtherInfoEt.text.toString()
            )
            Log.i("CreatePlayerFragment", "setSavePlayerListener: ${viewModel.player}")
        }
    }

    private fun getPlayerNumber(): Int {
        return try {
            binding.playerNumberEt.text.toString().toInt()
        }catch (e: Exception){
            0
        }
    }




}