package com.erasmus.upv.eps.wearables.ui.fragments

import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.erasmus.upv.eps.wearables.MainActivity
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentScanningBluetoothBinding
import com.erasmus.upv.eps.wearables.model.BLEDeviceWithGestures
import com.erasmus.upv.eps.wearables.model.Team
import com.erasmus.upv.eps.wearables.service.BLEConnectionForegroundService
import com.erasmus.upv.eps.wearables.ui.adapters.ScanResultsAdapter
import com.erasmus.upv.eps.wearables.util.BLEConnectionManager
import com.erasmus.upv.eps.wearables.util.DialogBuilder
import com.erasmus.upv.eps.wearables.viewModels.ReceivingDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ScanningBluetoothFragment : Fragment() {



    private lateinit var binding: FragmentScanningBluetoothBinding

    private lateinit var scanResultsAdapter: ScanResultsAdapter
    private val viewModel: ReceivingDataViewModel by hiltNavGraphViewModels(R.id.receiving_data_nested_graph)

    companion object{
        private const val TAG = "ScanningBluetoothFrag"
        private const val ENABLE_BLUETOOTH_REQUEST_CODE = 1
        private const val LOCATION_PERMISSION_REQUEST_CODE = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanningBluetoothBinding.inflate(inflater, container, false)

        receiveSafeArgs()
        clearDevicesLists()
        setUpRecyclerView()
        handleGoingToConfigurationDevices()
        handleScanButton()
        setUpSavedDevicesRecyclerView()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setUpSavedDevicesRecyclerView() {
        val rv = binding.savedDevicesRv
        rv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ScanResultsAdapter(this::onClickScanResultsAdapter)
        rv.adapter = adapter
        viewModel.getDevicesWithGestures()
        viewModel.savedDevicesAndGestures.observe(viewLifecycleOwner){
            adapter.submitList(getBluetoothDevicesFromAddresses(it).toMutableList())
        }
    }

    private fun getBluetoothDevicesFromAddresses(devicesWithGestures: List<BLEDeviceWithGestures>): List<BluetoothDevice> {
        return devicesWithGestures.map {
            BluetoothAdapter.getDefaultAdapter().getRemoteDevice(it.bleDevice.address)
        }
    }

    private fun handleScanButton() {
        binding.scanBt.setOnClickListener {
            if (isScanning) {
                stopBLEScan()
            } else {
                startBLEScan()
            }
        }
    }

    private fun handleGoingToConfigurationDevices() {
        binding.configureDevicesBt.setOnClickListener {
            Timber.d("handleGoingToConfigurationDevices: ${viewModel.selectedScanResults}")

            if(viewModel.selectedScanResults.isEmpty()){
                Toast.makeText(requireContext(), "Choose BLE Devices to connect with", Toast.LENGTH_SHORT).show()
            }else{
//                for (device in viewModel.selectedScanResults) {
//                    val bluetoothGatt = device.connectGatt(requireContext(), false, BLEConnectionManager.gattCallback)
//                    val address = bluetoothGatt.device.address
//                    if(address != null) {
//                        BLEConnectionForegroundService.gattDevicesMap[address] = bluetoothGatt
//                    }
//                }
                findNavController().navigate(R.id.action_scanningBluetoothFragment_to_configureDevicesFragment)
            }
        }
    }

    private fun setUpRecyclerView() {
        val scanResultRecyclerView = binding.scanResultsRv
        scanResultRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        scanResultsAdapter = ScanResultsAdapter(this::onClickScanResultsAdapter)
        scanResultRecyclerView.adapter = scanResultsAdapter
    }

    private fun onClickScanResultsAdapter(it: BluetoothDevice) {
        if (viewModel.selectedScanResults.contains(it)) {
            viewModel.selectedScanResults.remove(it)
        } else {
            viewModel.selectedScanResults.add(it)
        }
    }

    private fun clearDevicesLists() {
        viewModel.selectedScanResults = mutableListOf<BluetoothDevice>()
        BLEConnectionForegroundService.gattDevicesMap.clear()
    }

    private fun receiveSafeArgs() {
        if (arguments != null) {
            val args = ScanningBluetoothFragmentArgs.fromBundle(requireArguments())
            viewModel.matchId = args.matchId
            binding.configureDevicesBt.isEnabled = false
            binding.scanBt.isEnabled = false
            getMatchDetails()
        }
    }

    private fun getMatchDetails() {
        viewModel.getMatchWithTeams().observe(viewLifecycleOwner) {
            viewModel.match = it.match
            getHomeTeamWithPlayers(it.teams)
            getGuestTeamWithPlayers(it.teams)
            binding.configureDevicesBt.isEnabled = true
            binding.scanBt.isEnabled = true
        }
    }

    private fun getGuestTeamWithPlayers(teams: List<Team>) {
        viewModel.getTeamWithPlayers(teams[1].teamId).observe(viewLifecycleOwner){
            viewModel.guestTeam = it
        }
    }

    private fun getHomeTeamWithPlayers(teams: List<Team>) {
        viewModel.getTeamWithPlayers(teams[0].teamId).observe(viewLifecycleOwner){
            viewModel.homeTeam = it
        }
    }


    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = (requireActivity() as MainActivity).getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }


    // Enable bluetooth if it is disabled
    private fun promptEnableBluetooth(){
        if(!bluetoothAdapter.isEnabled){
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            bleResultLauncher.launch(enableBluetoothIntent)
        }
    }

    //check every time when fragment is shown if bluetooth is enabled
    override fun onResume() {
        super.onResume()
        promptEnableBluetooth()
    }

    var bleResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            promptEnableBluetooth()
        }
    }



    private val hasLocationPermissionGranted
        get() = ContextCompat.checkSelfPermission(requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED



    private fun startBLEScan(){
        informUserToTurnOnLocation()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasLocationPermissionGranted){
            requestLocationPermission()
        }else if(isLocationEnabled || Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            viewModel.clearScanResults()
            bleScanner.startScan(null, scanSettings, scanCallback) // TODO add filters to scan only for our watches
            isScanning = true

        }


    }


    private val isLocationEnabled: Boolean
        get() {
            val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }


    private fun informUserToTurnOnLocation() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && hasLocationPermissionGranted && !isLocationEnabled){
            DialogBuilder.buildAndShowDialog(
                requireContext(),
                requireActivity().layoutInflater,
                getString(R.string.ble_scan_requires_location),
                getString(R.string.ble_scan_requirement_message),
                getString(R.string.ok),
                yesButtonAction = { dialog -> dialog.dismiss() },
                isNoButtonVisible = false
            )
        }
    }

    override fun onStop() {
        super.onStop()
        stopBLEScan()
    }

    private fun stopBLEScan(){
        bleScanner.stopScan(scanCallback)
        isScanning = false
    }

    private var isScanning = false
        set(value) {
            field = value
            requireActivity().runOnUiThread {
                binding.scanBt.text = if(value) "Stop scan" else "Start scan"
            }
        }


    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private val scanCallback = object : ScanCallback(){

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            val device = result.device
            if(!viewModel.isDeviceAlreadySaved(device) && device.name != null){
                viewModel.addNewScanResult(device)
                viewModel.scanResultsLiveData.observe(viewLifecycleOwner){
                    scanResultsAdapter.submitList(it.toMutableList())
                }

            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Toast.makeText(requireContext(), "Scan failed", Toast.LENGTH_SHORT).show()
        }
    }







    private fun requestLocationPermission() {
        if(hasLocationPermissionGranted){
            return
        }

        requireActivity().runOnUiThread {


            DialogBuilder.buildAndShowDialog(
                requireContext(),
                requireActivity().layoutInflater,
                getString(R.string.location_permission_required),
                getString(R.string.location_permission_message),
                yesButtonAction = { dialog -> ActivityCompat.requestPermissions(
                    requireActivity() as MainActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
                    dialog.dismiss()
                },
                noButtonAction = { dialog -> dialog.dismiss() }
            )

        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            LOCATION_PERMISSION_REQUEST_CODE ->{
                if(grantResults.firstOrNull() == PackageManager.PERMISSION_DENIED){
                    requestLocationPermission()
                }else{
                    startBLEScan()
                }
            }
        }


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.scan_devices_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.delete_all_saved_devices -> {
                viewModel.deleteAllDevicesAndGestures()
                Toast.makeText(requireContext(), "Devices deleted", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}