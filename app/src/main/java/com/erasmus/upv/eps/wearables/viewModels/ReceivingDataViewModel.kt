package com.erasmus.upv.eps.wearables.viewModels

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.PrimaryKey
import com.erasmus.upv.eps.wearables.db.dao.MatchDao
import com.erasmus.upv.eps.wearables.model.*
import com.erasmus.upv.eps.wearables.model.actions.BasketballActions
import com.erasmus.upv.eps.wearables.model.actions.FootballActions
import com.erasmus.upv.eps.wearables.model.actions.HandballActions
import com.erasmus.upv.eps.wearables.repositories.MatchRepository
import com.erasmus.upv.eps.wearables.repositories.TeamRepository
import com.erasmus.upv.eps.wearables.util.Sports
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReceivingDataViewModel
    @Inject constructor(
        private val matchRepository: MatchRepository,
        private val teamRepository: TeamRepository
        ): ViewModel() {


    private var scanResults = mutableListOf<BluetoothDevice>()

    var selectedScanResults = mutableListOf<BluetoothDevice>()

    val scanResultsLiveData = MutableLiveData<List<BluetoothDevice>>()

    var matchId = 0L

    var match = Match()
    var homeTeam = TeamWithPlayers(Team(), listOf())
    var guestTeam = TeamWithPlayers(Team(), listOf())

    var selectedTeam = ""
    var selectedPlayer = ""
    var selectedAction = ""


    init {
        scanResultsLiveData.value = ArrayList()
    }


    fun addNewScanResult(device: BluetoothDevice){
        scanResults.add(device)
        scanResultsLiveData.value = scanResults
    }

    fun isDeviceAlreadySaved(device: BluetoothDevice): Boolean {
        return scanResults.indexOfFirst { it.address == device.address } != -1
    }

    fun clearScanResults() {
        scanResults.clear()
        scanResultsLiveData.value = scanResults
    }


    private fun createBLEDevice(it: BluetoothDevice): BLEDevice {
        var name = it.name
        if (name == null) {
            name = "Unknown Device"
        }
        return BLEDevice(it.address, name)
    }

    fun getSelectedBLEDevicesWithGestures(): List<BLEDeviceWithGestures> {
        return selectedScanResults.map {
            BLEDeviceWithGestures(
                    createBLEDevice(it),
                    getDefaultGesturesListForDevice(it.address)
            )
        }
    }

    private fun getDefaultGesturesListForDevice(deviceId: String): List<Gesture>{
        val gestures =  mutableListOf<Gesture>()
        for (i in 1..10){
            gestures.add(
                    Gesture(0L, "Gesture $i", i, null, 0L, 0L, deviceId)
            )
        }
        return gestures
    }


    fun getMatchWithTeams(): LiveData<MatchWithTeams>{
        return matchRepository.getMatchAndTeamsById(matchId)
    }

    fun getTeamWithPlayers(teamId: Long): LiveData<TeamWithPlayers> {
        return teamRepository.getTeamWithPlayers(teamId)
    }

    fun getActionsForAMatch(): List<String> {
        return when(match.sport){
            Sports.FOOTBALL -> FootballActions.values().map { it.name }
            Sports.HANDBALL -> HandballActions.values().map { it.name }
            else -> BasketballActions.values().map { it.name }
        }
    }


}

