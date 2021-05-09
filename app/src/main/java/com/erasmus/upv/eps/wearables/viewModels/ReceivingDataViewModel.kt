package com.erasmus.upv.eps.wearables.viewModels

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.PrimaryKey
import com.erasmus.upv.eps.wearables.db.dao.MatchDao
import com.erasmus.upv.eps.wearables.model.*
import com.erasmus.upv.eps.wearables.model.actions.Actions
import com.erasmus.upv.eps.wearables.model.actions.BasketballActions
import com.erasmus.upv.eps.wearables.model.actions.FootballActions
import com.erasmus.upv.eps.wearables.model.actions.HandballActions
import com.erasmus.upv.eps.wearables.repositories.ConfigRepository
import com.erasmus.upv.eps.wearables.repositories.MatchRepository
import com.erasmus.upv.eps.wearables.repositories.TeamRepository
import com.erasmus.upv.eps.wearables.service.BLEConnectionForegroundService
import com.erasmus.upv.eps.wearables.util.DateTimeFormatter
import com.erasmus.upv.eps.wearables.util.Sports
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class ReceivingDataViewModel
    @Inject constructor(
        private val matchRepository: MatchRepository,
        private val teamRepository: TeamRepository,
        private val configRepository: ConfigRepository
        ): ViewModel() {


    private var scanResults = mutableListOf<BluetoothDevice>()

    var selectedScanResults = mutableListOf<BluetoothDevice>()

    val scanResultsLiveData = MutableLiveData<List<BluetoothDevice>>()

    var matchId = 0L

    var match = Match()
    var homeTeam = TeamWithPlayers(Team(), listOf())
    var guestTeam = TeamWithPlayers(Team(), listOf())

    var selectedTeamId = 0L
    var selectedPlayerId = 0L
    var selectedAction: Actions? = null

    var devicesWithGestures = mutableListOf<BLEDeviceWithGestures>()

    val savedDevicesAndGestures = MutableLiveData<List<BLEDeviceWithGestures>>()


    val liveActions = MutableLiveData<MutableList<LiveAction>>()


    fun setDevicesWithGestures(){
        val selectedDevices = getSelectedBLEDevicesWithGestures().toMutableList()
        if(savedDevicesAndGestures.value != null) {
            for (savedDevice in savedDevicesAndGestures.value!!){
                if(selectedDevices.removeIf { it.bleDevice.address == savedDevice.bleDevice.address }) {
                    selectedDevices.add(savedDevice)
                }
            }
//
//                devicesWithGestures.addAll(getSelectedBLEDevicesWithGestures())
//            devicesWithGestures.addAll(savedDevicesAndGestures.value!!)
        }
        devicesWithGestures.clear()
        devicesWithGestures.addAll(selectedDevices)
    }


    init {
        scanResultsLiveData.value = ArrayList()
        liveActions.value = ArrayList()
    }


    fun addNewScanResult(device: BluetoothDevice){
        if(savedDevicesAndGestures.value != null) {
            for (savedDevice in savedDevicesAndGestures.value!!){
                if(savedDevice.bleDevice.address == device.address){
                    return
                }
            }
        }
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

    private fun getSelectedBLEDevicesWithGestures(): List<BLEDeviceWithGestures> {
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

    fun getActionsForAMatch(): List<Actions> {
        return when(match.sport){
            Sports.FOOTBALL -> FootballActions.values().toList()
            Sports.HANDBALL -> HandballActions.values().toList()
            else -> BasketballActions.values().toList()
        }
    }

    fun getPlayersFromSelectedTeam(): List<Player> {
        return when(selectedTeamId) {
            homeTeam.team.teamId -> homeTeam.players
            guestTeam.team.teamId -> guestTeam.players
            else -> emptyList()
        }
    }

    fun saveDevicesAndGestureConfiguration() {
        viewModelScope.launch {
//            configRepository.insertBLEDevice(devicesWithGestures.map { it.bleDevice })
//            val gestures = mutableListOf<Gesture>()
//            for(device in devicesWithGestures) {
//                gestures.addAll(device.gestures)
//            }
//            configRepository.insertGesturesConfig(gestures)
            configRepository.insertBLEDeviceWithGesture(devicesWithGestures)
        }
    }

    fun getDevicesWithGestures(){
        viewModelScope.launch {
            savedDevicesAndGestures.value = configRepository.getBLEDevicesWithGestures()
        }
    }


    fun deleteAllDevicesAndGestures(){
        viewModelScope.launch {
            configRepository.deleteAllDevicesAndGestures()
            savedDevicesAndGestures.value = emptyList()
        }
    }

    fun convertReceivedDataToLiveActions(responses: MutableList<Response>): List<LiveAction> {
        return responses.map { response ->
            val matchTime = Date(response.timeStamp - BLEConnectionForegroundService.matchStartTime)
            val device = devicesWithGestures.first {
                response.device.address == it.bleDevice.address
            }
            val gesture = device.gestures.first {
                it.receivedData == (response.data % 10 + 1)
            }
            val action = gesture.action
            val teamId = gesture.assignTeamId
            val playerId = gesture.assignPlayerId
            // TODO fix setting gestures, it was set as the previous one when user click to configure it but didn't
            Timber.d("\nreceived data: \n action = $action \n teamId = $teamId \n playerId = $playerId")
            if(teamId == 0L && playerId == 0L){
                return@map LiveAction(DateTimeFormatter.displayMinutesAndSeconds(matchTime.time), action.toString(), response.device.name)
            }

            if(teamId != 0L){
                // TODO show dialog for choosing teams
            }

            if(playerId != 0L){
                // TODO show dialog for choosing player
            }

            LiveAction(DateTimeFormatter.displayMinutesAndSeconds(matchTime.time), action.toString(), response.device.name)

        }
    }

    val askedTeamId = MutableLiveData<Long>()
    private lateinit var lastData: Response
    private lateinit var lastGesture: Gesture

    fun addNewLiveAction(lastData: Response) {
        this.lastData = lastData

        val device = devicesWithGestures.first {
            lastData.device.address == it.bleDevice.address
        }
        val gesture = device.gestures.first {
            it.receivedData == (lastData.data % 10 + 1)
        }
        lastGesture = gesture
        if(gesture.assignTeamId == 0L){
            askedTeamId.value = 0L
        }else {
            askedTeamId.value = lastGesture.assignTeamId
            saveLastAction()
        }
    }

    private fun saveLastAction() {
        val matchTime = Date(lastData.timeStamp - BLEConnectionForegroundService.matchStartTime)
        val extraInfo = formExtraInfo()
        val lastLiveAction = LiveAction(DateTimeFormatter.displayMinutesAndSeconds(matchTime.time), lastGesture.action.toString(), extraInfo)
        Timber.d("last live action $lastLiveAction")
        liveActions.value?.add(0, lastLiveAction)
        liveActions.notifyObserver()
    }

    private fun formExtraInfo():String  {
        val teamName = if(askedTeamId.value == homeTeam.team.teamId){
            homeTeam.team.name
        }else{
            guestTeam.team.name
        }
        return "${this.lastData.device.name}, team = $teamName"
    }

    private fun <T> MutableLiveData<T>.notifyObserver(){
        this.value = this.value
    }

    fun selectHomeTeam() {
        askedTeamId.value = homeTeam.team.teamId
        saveLastAction()
    }

    fun selectGuestTeam() {
        askedTeamId.value = guestTeam.team.teamId
        saveLastAction()
    }


}

