package com.erasmus.upv.eps.wearables.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentCreateMatchBinding
import com.erasmus.upv.eps.wearables.databinding.FragmentCreatePlayerBinding
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.viewModels.CreateMatchViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CreateMatchFragment : Fragment() {

    private lateinit var binding: FragmentCreateMatchBinding
    private val viewModel: CreateMatchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateMatchBinding.inflate(inflater, container, false)

        handleDateInput()
        handleTimeInput()
        createMatch()

        return binding.root
    }

    private fun createMatch() {
        binding.doneCreatingMatchFb.setOnClickListener {
            viewModel.createMatch(getMatchFromUserInput())
            Toast.makeText(requireContext(), "Match created", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    private fun getMatchFromUserInput(): Match {
        return Match(
            0L,
            binding.matchDateEt.text.toString(),
            binding.matchTimeEt.text.toString(),
            binding.matchLocationEt.text.toString(),
            getSelectedSport(),
            binding.matchLeagueEt.text.toString(),
            binding.matchDetailsEt.text.toString()
        )
    }

    private fun getSelectedSport(): String  = when(binding.sportRadioGroup.checkedRadioButtonId){
        R.id.football_radio_button -> "Football"
        R.id.handball_radio_button -> "Handball"
        else -> "Basketball"
    }

    private fun handleTimeInput() {
        binding.matchTimeEt.setOnClickListener {
            createTimePicker()
        }
    }


    private fun handleDateInput() {
        binding.matchDateEt.setOnClickListener {
            createDataPicker()
        }
    }


    private fun createTimePicker() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                binding.matchTimeEt.setText("$hourOfDay:$minute")
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun createDataPicker() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                Log.i("CreateMatchFragment", "createDataPicker: $year - $month - $dayOfMonth")
                binding.matchDateEt.setText("$dayOfMonth-${month + 1}-$year")
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = Date().time
        datePickerDialog.show()

    }

}