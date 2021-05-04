package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentConfigureDevicesBinding
import com.erasmus.upv.eps.wearables.model.BLEDevice
import com.erasmus.upv.eps.wearables.ui.adapters.DevicesConfigurationAdapter
import com.erasmus.upv.eps.wearables.ui.dialogs.ConfigureGestureDialogFragment
import com.erasmus.upv.eps.wearables.viewModels.ReceivingDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ConfigureDevicesFragment : Fragment() {


    private lateinit var binding: FragmentConfigureDevicesBinding
    private lateinit var deviceAdapter: DevicesConfigurationAdapter
    private val viewModel: ReceivingDataViewModel by hiltNavGraphViewModels(R.id.receiving_data_nested_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfigureDevicesBinding.inflate(inflater, container, false)
        Timber.d("selected devices ${viewModel.selectedScanResults}")
        setUpRecyclerView()
        getSavedDeviceConfiguration()
        handleDoneButton()
        return binding.root
    }

    private fun getSavedDeviceConfiguration() {
        viewModel.getDevicesWithGestures()
        viewModel.savedDevicesAndGestures.observe(viewLifecycleOwner){
            viewModel.setDevicesWithGestures()
            deviceAdapter.notifyDataSetChanged()
            Timber.d("get selected and saved devices ${viewModel.devicesWithGestures}")
        }
    }


    private fun handleDoneButton() {
        binding.doneBt.setOnClickListener {
            Timber.d("check gesture config ${viewModel.devicesWithGestures}")
            viewModel.saveDevicesAndGestureConfiguration()
            findNavController().navigate(R.id.action_configureDevicesFragment_to_currentMatchFragment)
        }
    }

    private fun setUpRecyclerView() {
        val rv = binding.devicesConfigRv
        rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        deviceAdapter = DevicesConfigurationAdapter(viewModel.devicesWithGestures, requireContext()) { device, gesture ->
            ConfigureGestureDialogFragment(device, gesture).show(childFragmentManager, ConfigureGestureDialogFragment.TAG)
        }
        rv.adapter = deviceAdapter
    }


}