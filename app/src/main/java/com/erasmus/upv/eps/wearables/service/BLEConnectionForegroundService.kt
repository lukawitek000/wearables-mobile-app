package com.erasmus.upv.eps.wearables.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDeepLinkBuilder
import com.erasmus.upv.eps.wearables.MainActivity
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.model.Response
import com.erasmus.upv.eps.wearables.util.BLEConnectionManager
import timber.log.Timber

class BLEConnectionForegroundService : LifecycleService() {



    companion object{

        private const val NOTIFICATION_CHANNEL_NAME = "BLE_connection_notification_name"
        private const val NOTIFICATION_CHANNEL_ID = "BLE_connection_notification_channel_id"
        private const val NOTIFICATION_ID = 420


        const val START = "START"
        const val STOP = "STOP"

        val receiveData = MutableLiveData<MutableList<Response>>()

        var gattDevicesMap: HashMap<String, BluetoothGatt?> = HashMap<String, BluetoothGatt?>()


        var isServiceRunning: Boolean = false

    }

    private fun initValues(){
        receiveData.value = (emptyList<Response>().toMutableList())
        BLEConnectionManager.responseList.value?.clear()
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        initValues()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.i( "onStartCommand: $intent ${intent?.action}")
        intent?.let{
            when(intent.action){
                START -> {
                    connectGatt()
                    startForegroundService()
                }
                STOP -> {
                    stopService()
                }
                else -> Timber.i( "onStartCommand: wrong action")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun stopService(){
        isServiceRunning = false
        initValues()
        gattDevicesMap.forEach {
            it.value?.disconnect()
        }
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(true)
        stopSelf()
    }

    private fun connectGatt() {
        BLEConnectionManager.responseList.observe(this){
            receiveData.postValue(it)
        }
    }


    private fun startForegroundService(){
        isServiceRunning = true
        Timber.i("startForegroundService: start service")

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel()
        }
        startForeground(NOTIFICATION_ID, getNotificationBuilder().build())

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }


    private lateinit var notificationManager: NotificationManager


    private fun getNotificationBuilder(): NotificationCompat.Builder{
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(false)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("BLE Connection")
                .setContentText("BLEDevice connected to ....")
                .setContentIntent(getPendingIntent())
    }

    private fun getPendingIntent(): PendingIntent {
        return NavDeepLinkBuilder(applicationContext)
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.currentMatchFragment)
                .createPendingIntent()
    }






}