package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentCreatePlayerBinding
import com.erasmus.upv.eps.wearables.viewModels.CreatePlayerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        receiveSafeArgs()


        return binding.root
    }

    private fun receiveSafeArgs() {
        if (arguments != null) {
            val args = CreatePlayerFragmentArgs.fromBundle(requireArguments())
            if (args.playerId > 0L) {
                viewModel.getPlayerById(args.playerId).observe(viewLifecycleOwner) {
                    viewModel.player = it
                    populateInputs()
                    changeButtonText()
                }
            } else {
                findNavController().navigateUp()
            }
        }
    }

    private fun changeButtonText() {
        binding.savePlayerBt.text = "Update"
    }

    private fun populateInputs() {
        binding.sportRadioGroup.check(setPlayerSport())
        binding.playerNameEt.setText(viewModel.player.name)
        binding.playerNumberEt.setText(viewModel.player.number.toString())
        binding.playerPositionEt.setText(viewModel.player.position)
        binding.playerOtherInfoEt.setText(viewModel.player.otherInfo)
    }

    private fun setPlayerSport(): Int {
        return when(viewModel.player.sport){
            "Football" -> R.id.football_radio_button
            "Basketball" -> R.id.basketball_radio_button
            else -> R.id.handball_radio_button
        }
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
            if(viewModel.player.playerId == 0L){
                savePlayer()
            }else{
                updatePlayer()
            }

            findNavController().navigateUp()
        }
    }

    private fun updatePlayer() {
        viewModel.updatePlayer(
                binding.playerNameEt.text.toString(),
                getPlayerNumber(),
                binding.playerPositionEt.text.toString(),
                binding.playerOtherInfoEt.text.toString()
        )
    }

    private fun savePlayer() {
        viewModel.createPlayer(
                binding.playerNameEt.text.toString(),
                getPlayerNumber(),
                binding.playerPositionEt.text.toString(),
                binding.playerOtherInfoEt.text.toString()
        )
        Log.i("CreatePlayerFragment", "setSavePlayerListener: ${viewModel.player}")
        Toast.makeText(requireContext(), "Player saved", Toast.LENGTH_SHORT).show()
    }

    private fun getPlayerNumber(): Int {
        return try {
            binding.playerNumberEt.text.toString().toInt()
        }catch (e: Exception){
            0
        }
    }




}