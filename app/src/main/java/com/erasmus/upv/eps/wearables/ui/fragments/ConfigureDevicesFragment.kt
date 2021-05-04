package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.databinding.FragmentConfigureDevicesBinding
import com.erasmus.upv.eps.wearables.model.BLEDevice
import com.erasmus.upv.eps.wearables.ui.adapters.DevicesConfigurationAdapter
import com.erasmus.upv.eps.wearables.ui.dialogs.ConfigureGestureDialogFragment
import com.erasmus.upv.eps.wearables.viewModels.ReceivingDataViewModel
import timber.log.Timber


class ConfigureDevicesFragment : Fragment() {


    private lateinit var binding: FragmentConfigureDevicesBinding

    private val viewModel: ReceivingDataViewModel by navGraphViewModels(R.id.receiving_data_nested_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfigureDevicesBinding.inflate(inflater, container, false)
        Timber.d("selected devices ${viewModel.selectedScanResults}")
        setUpRecyclerView()
        handleDoneButton()
        return binding.root
    }


    private fun handleDoneButton() {
        binding.doneBt.setOnClickListener {
            findNavController().navigate(R.id.action_configureDevicesFragment_to_currentMatchFragment)
        }
    }

    private fun setUpRecyclerView() {
        val rv = binding.devicesConfigRv
        rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv.adapter = DevicesConfigurationAdapter(viewModel.getSelectedBLEDevicesWithGestures(), requireContext()) { device, gesture ->
            ConfigureGestureDialogFragment(device, gesture).show(childFragmentManager, ConfigureGestureDialogFragment.TAG)
        }
    }


}

private val devices = emptyList<BLEDevice>()
// listOf<BLEDevice>(
//        BLEDevice(
//                0L,
//                "BLEDevice 0L",
//                listOf(
//                        Gesture(0L, "Gesture 0", "Some action"),
//                        Gesture(1L, "Gesture 1", "Some action"),
//                        Gesture(2L, "Gesture 2", "Some action"),
//                        Gesture(3L, "Gesture 3", "Some action"),
//                        Gesture(4L, "Gesture 4", "Some action"),
//                        Gesture(5L, "Gesture 5", "Some action"),
//                        Gesture(6L, "Gesture 6", "Some action"),
//                        Gesture(7L, "Gesture 7", "Some action"),
//                )
//        ),
//        BLEDevice(
//                1L,
//                "BLEDevice 1L",
//                listOf(
//                        Gesture(10L, "Gesture 10", "Some action"),
//                        Gesture(11L, "Gesture 11", "Some action"),
//                        Gesture(12L, "Gesture 12", "Some action"),
//                        Gesture(13L, "Gesture 13", "Some action"),
//                        Gesture(14L, "Gesture 14", "Some action"),
//                        Gesture(15L, "Gesture 15", "Some action"),
//                        Gesture(16L, "Gesture 16", "Some action"),
//                        Gesture(17L, "Gesture 17", "Some action"),
//                )
//        ),
//        BLEDevice(
//                2L,
//                "BLEDevice 2L",
//                listOf(
//                        Gesture(20L, "Gesture 20", "Some action"),
//                        Gesture(21L, "Gesture 21", "Some action"),
//                        Gesture(22L, "Gesture 22", "Some action"),
//                        Gesture(23L, "Gesture 23", "Some action"),
//                        Gesture(24L, "Gesture 24", "Some action"),
//                        Gesture(25L, "Gesture 25", "Some action"),
//                        Gesture(26L, "Gesture 26", "Some action"),
//                        Gesture(27L, "Gesture 27", "Some action"),
//                )
//        ),
//        BLEDevice(
//                3L,
//                "BLEDevice 3L",
//                listOf(
//                        Gesture(30L, "Gesture 30", "Some action"),
//                        Gesture(31L, "Gesture 31", "Some action"),
//                        Gesture(32L, "Gesture 32", "Some action"),
//                        Gesture(33L, "Gesture 33", "Some action"),
//                        Gesture(34L, "Gesture 34", "Some action"),
//                        Gesture(35L, "Gesture 35", "Some action"),
//                        Gesture(36L, "Gesture 36", "Some action"),
//                        Gesture(37L, "Gesture 37", "Some action"),
//                )
//        ),
//        BLEDevice(
//                4L,
//                "BLEDevice 4L",
//                listOf(
//                        Gesture(40L, "Gesture 40", "Some action"),
//                        Gesture(41L, "Gesture 41", "Some action"),
//                        Gesture(42L, "Gesture 42", "Some action"),
//                        Gesture(43L, "Gesture 43", "Some action"),
//                        Gesture(44L, "Gesture 44", "Some action"),
//                        Gesture(45L, "Gesture 45", "Some action"),
//                        Gesture(46L, "Gesture 46", "Some action"),
//                        Gesture(47L, "Gesture 47", "Some action"),
//                )
//        ),
//        BLEDevice(
//                5L,
//                "BLEDevice 5L",
//                listOf(
//                        Gesture(50L, "Gesture 50", "Some action"),
//                        Gesture(51L, "Gesture 51", "Some action"),
//                        Gesture(52L, "Gesture 52", "Some action"),
//                        Gesture(53L, "Gesture 53", "Some action"),
//                        Gesture(54L, "Gesture 54", "Some action"),
//                        Gesture(55L, "Gesture 55", "Some action"),
//                        Gesture(56L, "Gesture 56", "Some action"),
//                        Gesture(57L, "Gesture 57", "Some action"),
//                )
//        ),
//)