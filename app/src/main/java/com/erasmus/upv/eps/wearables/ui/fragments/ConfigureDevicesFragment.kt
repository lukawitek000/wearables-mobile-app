package com.erasmus.upv.eps.wearables.ui.fragments

import android.app.AlertDialog
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentConfigureDevicesBinding
import com.erasmus.upv.eps.wearables.model.BLEDevice
import com.erasmus.upv.eps.wearables.model.Gesture
import com.erasmus.upv.eps.wearables.service.BLEConnectionForegroundService
import com.erasmus.upv.eps.wearables.ui.adapters.DevicesConfigurationAdapter
import com.erasmus.upv.eps.wearables.ui.dialogs.ConfigureGestureDialogFragment
import com.erasmus.upv.eps.wearables.util.BLEConnectionManager
import com.erasmus.upv.eps.wearables.util.DialogBuilder
import com.erasmus.upv.eps.wearables.viewModels.ReceivingDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


//TODO: Keep position of recycler view when gesture is configured

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
        connectToSelectedDevices()
        setUpCustomBackPress()
        observeGattStatusChanges()
        return binding.root
    }




    private fun setUpCustomBackPress() {
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                BLEConnectionManager.disconnectAllDevices()
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun connectToSelectedDevices() {
        for (device in viewModel.selectedScanResults) {
            connectBLEDevice(device)
//            val address = bluetoothGatt.device.address
//            if(address != null) {
//                BLEConnectionForegroundService.gattDevicesMap[address] = bluetoothGatt
//            }
        }
    }

    private fun connectBLEDevice(device: BluetoothDevice) {
        val bluetoothGatt =
            device.connectGatt(requireContext(), false, BLEConnectionManager.gattCallback)
        BLEConnectionManager.updateGattStatus(
            bluetoothGatt.device.address,
            BLEConnectionManager.GattStatus.CONNECTING
        )
    }

    private fun getSavedDeviceConfiguration() {
        viewModel.getDevicesWithGestures()
        viewModel.savedDevicesAndGestures.observe(viewLifecycleOwner){
            viewModel.setDevicesWithGestures()
            if(viewModel.isConfigResetForUnknownTeam){
                showDialogAboutResettingConfiguration()
            }
            deviceAdapter.notifyDataSetChanged()
            Timber.d("get selected and saved devices ${viewModel.devicesWithGestures}")
        }
    }

    private fun showDialogAboutResettingConfiguration() {
        DialogBuilder.buildAndShowDialog(
            requireContext(),
            requireActivity().layoutInflater,
            getString(R.string.info),
            getString(R.string.reset_config_message),
            getString(R.string.ok),
            isNoButtonVisible = false,
            yesButtonAction = { dialog -> dialog.dismiss() },
        )
    }


    private fun handleDoneButton() {
        binding.doneBt.setOnClickListener {
            Timber.d("check gesture config ${viewModel.devicesWithGestures}")
            viewModel.saveDevicesAndGestureConfiguration()
            BLEConnectionManager.discoverServicesConnectedDevices()
            findNavController().navigate(R.id.action_configureDevicesFragment_to_currentMatchFragment)
        }
    }

    private fun setUpRecyclerView() {
        val rv = binding.devicesConfigRv
        rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        deviceAdapter = DevicesConfigurationAdapter(viewModel.devicesWithGestures, requireContext(),
            onGestureClick = this::onGestureClick, reconnectDevice = this::reconnectBLEDevice)
        rv.adapter = deviceAdapter
    }


    private fun updateColor(){
        deviceAdapter.notifyDataSetChanged()
    }

    private fun onGestureClick(device: BLEDevice, gesture: Gesture){
        ConfigureGestureDialogFragment(device, gesture, this::updateColor).show(childFragmentManager, ConfigureGestureDialogFragment.TAG)
    }

    private fun reconnectBLEDevice(address: String){
        val bluetoothDevice = viewModel.selectedScanResults.firstOrNull {
            it.address == address
        } ?: return
        connectBLEDevice(bluetoothDevice)
    }


    private fun observeGattStatusChanges() {
        BLEConnectionManager.bluetoothGattsStatus.observe(viewLifecycleOwner){
            Timber.d("status changed ${it.values.map { status -> status.name }}")
            deviceAdapter.notifyDataSetChanged()
            binding.doneBt.isEnabled = it.values.contains(BLEConnectionManager.GattStatus.CONNECTED)
        }
    }

}