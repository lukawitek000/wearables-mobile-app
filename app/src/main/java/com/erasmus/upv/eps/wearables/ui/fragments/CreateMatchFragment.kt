package com.erasmus.upv.eps.wearables.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentCreateMatchBinding
import com.erasmus.upv.eps.wearables.databinding.FragmentCreatePlayerBinding
import java.util.*

class CreateMatchFragment : Fragment() {

    private lateinit var binding: FragmentCreateMatchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateMatchBinding.inflate(inflater, container, false)

        handleDateInput()

        return binding.root
    }

    private fun handleDateInput() {
        binding.matchDateEt.setOnClickListener {
            createDataPicker()
        }
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