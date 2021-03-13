package com.erasmus.upv.eps.wearables.ui

import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erasmus.upv.eps.wearables.MainActivity
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.util.BLEConnectionManager
import java.security.Permission


class ScanningBluetoothFragment : Fragment() {


    private lateinit var scanResultsAdapter: ScanResultsAdapter
    private lateinit var viewModel: ScanningBluetoothViewModel
    private lateinit var scanButton: Button

    companion object{
        private const val TAG = "ScanningBluetoothFrag"
        private const val ENABLE_BLUETOOTH_REQUEST_CODE = 1
        private const val LOCATION_PERMISSION_REQUEST_CODE = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scanning_bluetooth, container, false)

        viewModel = ViewModelProvider(this).get(ScanningBluetoothViewModel::class.java)


        val scanResultRecyclerView = view.findViewById<RecyclerView>(R.id.scan_results_recyclerView)
        scanResultRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        scanResultsAdapter = ScanResultsAdapter(viewModel.scanResults){
            //TODO

            it.connectGatt(requireContext(), false, BLEConnectionManager.gattCallback)

            findNavController().navigate(R.id.action_scanningBluetoothFragment_to_responseDataListFragment)
            Toast.makeText(requireContext(), "${it.name} selected", Toast.LENGTH_SHORT).show()
        }
        scanResultRecyclerView.adapter = scanResultsAdapter


        scanButton = view.findViewById(R.id.scan_button)
        scanButton.setOnClickListener {
            if(isScanning){
                stopBLEScan()
            }else{
                startBLEScan()
            }
        }
        return view
    }


    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = (requireActivity() as MainActivity).getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }


    // Enable bluetooth if it is disabled
    private fun promptEnableBluetooth(){
        if(!bluetoothAdapter.isEnabled){
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, ENABLE_BLUETOOTH_REQUEST_CODE)
        }
    }

    override fun onResume() {
        super.onResume()
        promptEnableBluetooth()
    }


    //check if enabling bluetooth has succeed
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            ENABLE_BLUETOOTH_REQUEST_CODE -> {
                if(requestCode != Activity.RESULT_OK){
                    promptEnableBluetooth()
                }
            }
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
            viewModel.scanResults.clear()
            scanResultsAdapter.notifyDataSetChanged()

            bleScanner.startScan(null, scanSettings, scanCallback)
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
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("BLE scan requires location")
            builder.setMessage("Turn on Location in Settings to see scan results")
            val listener = DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() }
            builder.setPositiveButton("Ok", listener)
            builder.show()
        }
    }

    private fun stopBLEScan(){
        bleScanner.stopScan(scanCallback)
        isScanning = false
    }

    private var isScanning = false
        set(value) {
            field = value
            requireActivity().runOnUiThread {
                scanButton.text = if(value) "Stop scan" else "Start scan"
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
            val indexQuery = viewModel.scanResults.indexOfFirst { it.address == result.device.address }
            if(indexQuery == -1){
                viewModel.scanResults.add(result.device)
                scanResultsAdapter.notifyItemInserted(viewModel.scanResults.size - 1)
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Toast.makeText(requireContext(), "Scan failed", Toast.LENGTH_SHORT).show()
            Log.i(TAG, "onScanFailed: error $errorCode")
        }
    }







    private fun requestLocationPermission() {
        if(hasLocationPermissionGranted){
            return
        }

        requireActivity().runOnUiThread {

            val alertDialog = AlertDialog.Builder(requireContext())
            alertDialog.setTitle("Location permission required")
            alertDialog.setMessage("Starting from Android M (6.0), the system requires apps to be granted " +
                    "location access in order to scan for BLE devices.")
            alertDialog.setCancelable(false)
            val listener = DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(requireContext(), "Inside listener", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(
                    requireActivity() as MainActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
            alertDialog.setPositiveButton("OK", listener)
            alertDialog.show()
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
}