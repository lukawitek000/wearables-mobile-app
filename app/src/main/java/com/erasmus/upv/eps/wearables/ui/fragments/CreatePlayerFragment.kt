package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentCreatePlayerBinding
import com.erasmus.upv.eps.wearables.util.Sports
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
        binding = FragmentCreatePlayerBinding.inflate(inflater, container, false)
        observeUserInput()
        receiveSafeArgs()
        return binding.root
    }


    private fun receiveSafeArgs() {
        if (arguments != null) {
            val args = CreatePlayerFragmentArgs.fromBundle(requireArguments())
            if (args.playerId > 0L) {
                getPlayerFromDatabase(args.playerId)
            }
        }
    }


    private fun getPlayerFromDatabase(playerId: Long) {
        viewModel.getPlayerById(playerId).observe(viewLifecycleOwner) {
            viewModel.player = it
            populateInputs()
            changeSaveButtonText()
        }
    }


    private fun changeSaveButtonText() {
        binding.savePlayerBt.text = getString(R.string.update)
    }


    private fun populateInputs() {
        binding.radioButtonSelectSport.sportRadioGroup.check(setRadioButtonForPlayerSport())
        binding.playerNationalityEt.setText(viewModel.player.nationality)
        binding.playerNameEt.setText(viewModel.player.name)
        binding.playerNumberEt.setText(viewModel.player.number.toString())
        binding.playerPositionEt.setText(viewModel.player.position)
        binding.playerOtherInfoEt.setText(viewModel.player.otherInfo)
    }


    private fun setRadioButtonForPlayerSport(): Int {
        return when(viewModel.player.sport){
            Sports.FOOTBALL -> R.id.football_radio_button
            Sports.BASKETBALL -> R.id.basketball_radio_button
            else -> R.id.handball_radio_button
        }
    }


    private fun observeUserInput() {
        savePlayerSportFromRadioButton()
        getSportInput()
        setSavePlayerListener()
    }


    private fun savePlayerSportFromRadioButton() {
        viewModel.player.sport = getCheckedSport(binding.radioButtonSelectSport.sportRadioGroup.checkedRadioButtonId)
    }


    private fun getSportInput(){
        binding.radioButtonSelectSport.sportRadioGroup.setOnCheckedChangeListener { _, _ ->
            savePlayerSportFromRadioButton()
        }

    }


    private fun getCheckedSport(checkedId: Int) : String {
        return when (checkedId) {
            R.id.football_radio_button -> Sports.FOOTBALL
            R.id.basketball_radio_button -> Sports.BASKETBALL
            else -> Sports.HANDBALL
        }
    }


    private fun setSavePlayerListener(){
        binding.savePlayerBt.setOnClickListener {
            if(viewModel.isNewPlayerCreated()){
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
                binding.playerNationalityEt.text.toString(),
                binding.playerPositionEt.text.toString(),
                binding.playerOtherInfoEt.text.toString()
        )
        Toast.makeText(requireContext(), getString(R.string.player_updated), Toast.LENGTH_SHORT).show()
    }


    private fun savePlayer() {
        viewModel.createPlayer(
                binding.playerNameEt.text.toString(),
                getPlayerNumber(),
                binding.playerNationalityEt.text.toString(),
                binding.playerPositionEt.text.toString(),
                binding.playerOtherInfoEt.text.toString()
        )
        Toast.makeText(requireContext(), getString(R.string.player_created), Toast.LENGTH_SHORT).show()
    }


    private fun getPlayerNumber(): Int {
        return try {
            binding.playerNumberEt.text.toString().toInt()
        }catch (e: Exception){
            0
        }
    }

}