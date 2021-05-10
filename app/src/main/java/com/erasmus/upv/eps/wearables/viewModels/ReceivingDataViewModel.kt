package com.erasmus.upv.eps.wearables.viewModels

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.*
import androidx.room.PrimaryKey
import com.erasmus.upv.eps.wearables.db.dao.MatchDao
import com.erasmus.upv.eps.wearables.model.*
import com.erasmus.upv.eps.wearables.model.actions.Actions
import com.erasmus.upv.eps.wearables.model.actions.BasketballActions
import com.erasmus.upv.eps.wearables.model.actions.FootballActions
import com.erasmus.upv.eps.wearables.model.actions.HandballActions
import com.erasmus.upv.eps.wearables.repositories.ConfigRepository
import com.erasmus.upv.eps.wearables.repositories.MatchRepository
import com.erasmus.upv.eps.wearables.repositories.StatisticsRepository
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
        private val configRepository: ConfigRepository,
        private val statisticsRepository: StatisticsRepository
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


    fun getLiveActionsForCurrentMatch(): LiveData<List<LiveAction>>{
        return statisticsRepository.getLiveActionsForTheMatch(matchId).asLiveData()
    }


    init {
        scanResultsLiveData.value = ArrayList()
    }

    fun clearSelectedConfig() {
        selectedTeamId = 0L
        selectedPlayerId = 0L
        selectedAction = null
    }


    fun setDevicesWithGestures(){
        val selectedDevices = getSelectedBLEDevicesWithGestures().toMutableList()
        if(savedDevicesAndGestures.value != null) {
            for (savedDevice in savedDevicesAndGestures.value!!){
                if(selectedDevices.removeIf { it.bleDevice.address == savedDevice.bleDevice.address }) {
                    selectedDevices.add(savedDevice)
                }
            }
        }
        devicesWithGestures.clear()
        devicesWithGestures.addAll(selectedDevices)
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

    fun getPlayersNameFromSelectedTeam(): List<String> {
        return when(selectedTeamId) {
            homeTeam.team.teamId -> homeTeam.players.map { it.name }
            guestTeam.team.teamId -> guestTeam.players.map { it.name }
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


    val askedTeamId = MutableLiveData<Long>()
    val askedPlayerId = MutableLiveData<Long>()
    private lateinit var lastData: Response
    private lateinit var lastGesture: Gesture
    var isDataCleared = false

    fun getLastActionRecorded(): String{
        return lastGesture.action.toString()
    }


    fun addNewLiveAction(lastData: Response) {
        this.lastData = lastData

        val device = devicesWithGestures.first {
            lastData.device.address == it.bleDevice.address
        }
        val gesture = device.gestures.first {
            it.receivedData == (lastData.data % 10 + 1)
        }
        lastGesture = gesture
        when {
            gesture.assignTeamId == 0L -> {
                askedTeamId.value = 0L
            }
            gesture.assignPlayerId == 0L -> {
                askedTeamId.value = lastGesture.assignTeamId
                askedPlayerId.value = 0L
            }
            else -> {
                askedTeamId.value = lastGesture.assignTeamId
                askedPlayerId.value = lastGesture.assignPlayerId
                saveLastAction()
            }
        }
    }

    private fun saveLastAction() {
        viewModelScope.launch {
            val matchTime = Date(lastData.timeStamp - BLEConnectionForegroundService.matchStartTime)
            //  val extraInfo = formExtraInfo()
            val time = DateTimeFormatter.displayMinutesAndSeconds(matchTime.time)
//        val lastLiveAction = LiveAction(, lastGesture.action.toString(), extraInfo)
            val lastLiveAction = LiveAction(0L, matchId, askedTeamId.value ?: 0L,
                    askedPlayerId.value ?: 0L, time, lastGesture.action)
            Timber.d("last live action $lastLiveAction")
            clearTeamAndPlayerData()
            statisticsRepository.insertLastLiveAction(lastLiveAction)
//            liveActions.value?.add(0, lastLiveAction)
//            liveActions.notifyObserver()
        }
    }

    private fun clearTeamAndPlayerData() {
        isDataCleared = true
        askedPlayerId.value = 0L
        askedTeamId.value = 0L
        isDataCleared = false
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

    fun getPlayersFromChosenTeam(): List<Player> {
        return when (askedTeamId.value) {
            homeTeam.team.teamId -> {
                homeTeam.players
            }
            guestTeam.team.teamId -> {
                guestTeam.players
            }
            else -> {
                emptyList()
            }
        }
    }

    fun selectPlayer(playerId: Long) {
        askedPlayerId.value = playerId
        saveLastAction()
    }

    fun dismissSelectingPlayer() {
        saveLastAction()
    }

    fun deleteLiveActionById(id: Long) {
        viewModelScope.launch {
            statisticsRepository.deleteLiveActionById(id)
        }
    }

    fun getTeamNameById(assignTeamId: Long): CharSequence? {
        return when (assignTeamId) {
            homeTeam.team.teamId -> {
                homeTeam.team.name
            }
            guestTeam.team.teamId -> {
                guestTeam.team.name
            }
            else -> {
                null
            }
        }
    }

    fun getTeamIdByTeamName(teamName: String?): Long {
        return when (teamName) {
            homeTeam.team.name -> {
                homeTeam.team.teamId
            }
            guestTeam.team.name -> {
                guestTeam.team.teamId
            }
            else -> {
                0L
            }
        }
    }

    fun getPlayerNameById(playerId: Long): CharSequence? {
        return when {
            homeTeam.players.count { player -> player.playerId == playerId } > 0 -> {
                homeTeam.players.find { it.playerId == playerId }?.name
            }
            guestTeam.players.count { player -> player.playerId == playerId } > 0 -> {
                guestTeam.players.find { it.playerId == playerId }?.name
            }
            else -> {
                null
            }
        }
    }

    fun getPlayerIdByPlayerName(playerName: String?): Long {
        return when {
            homeTeam.players.any { it.name == playerName } -> {
                homeTeam.players.find { it.name == playerName }?.playerId ?: 0L
            }
            guestTeam.players.any { it.name == playerName } -> {
                guestTeam.players.find { it.name == playerName }?.playerId ?: 0L
            }
            else -> {
                0L
            }
        }
    }


}

