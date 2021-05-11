package com.erasmus.upv.eps.wearables.util

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

object MatchTimer {


    val matchTimeInMillis =  MutableLiveData<Long>()


    private var intervalDuration = 0L
    private var intervals = 0

    private var job: Deferred<Unit>? = null

    private val secondInMillis = 1000L

    val isMatchIntervalCompleted = MutableLiveData<Boolean>()



    fun configTimer(intervalDurationInSeconds: Long, intervals: Int){
        this.intervalDuration = intervalDurationInSeconds
        this.intervals = intervals
        matchTimeInMillis.value = 0L
        isMatchIntervalCompleted.value = false
    }

    fun isTimerRunning(): Boolean{
        return job?.isActive ?: false
    }


    fun startTimer(){
        job = CoroutineScope(Dispatchers.Default).launchPeriodicAsync(secondInMillis){
            matchTimeInMillis.postValue(matchTimeInMillis.value?.plus(secondInMillis))
            if(matchTimeInMillis.value!! >= (intervalDuration * secondInMillis) - secondInMillis){
                isMatchIntervalCompleted.postValue(true)
                job?.cancel()
            }
        }


    }


    fun stopTimer(){
        job?.cancel()
    }

    fun pauseTimer(){

    }


    private fun CoroutineScope.launchPeriodicAsync(
        repeatMillis: Long,
        action: () -> Unit
    ) = this.async {
        if (repeatMillis > 0) {
            while (isActive) {
                delay(repeatMillis)
                action()
            }
        } else {
            action()
        }
    }






}