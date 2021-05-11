package com.erasmus.upv.eps.wearables.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

object MatchTimer {


    val matchTimeInSeconds =  MutableLiveData<Long>()


    private var intervalDuration = 0L
    private var intervals = 0

    private var job: Deferred<Unit>? = null



    fun configTimer(intervalDuration: Long, intervals: Int){
        this.intervalDuration = intervalDuration
        this.intervals = intervals
        matchTimeInSeconds.value = 0L
    }

    fun isTimerRunning(): Boolean{
        return job?.isActive ?: false
    }


    fun startTimer(){
        job = CoroutineScope(Dispatchers.Default).launchPeriodicAsync(1000L){
            matchTimeInSeconds.postValue(matchTimeInSeconds.value?.plus(1))
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