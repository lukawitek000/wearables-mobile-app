package com.erasmus.upv.eps.wearables.util

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

object MatchTimer {


    val matchTimeInMillis =  MutableLiveData<Long>()


    private var intervalDuration = 0L
    var intervalsLeft = 0

    private var job: Deferred<Unit>? = null

    private const val secondInMillis = 1000L

    private var intervals = 0

    val isMatchIntervalCompleted = MutableLiveData<Boolean>(false)

    var isTimerPaused = false



    fun configTimer(intervalDurationInSeconds: Long, intervals: Int){
        this.intervalDuration = intervalDurationInSeconds * secondInMillis
        this.intervalsLeft = intervals
        this.intervals = intervals
        matchTimeInMillis.value = 0L
        isMatchIntervalCompleted.value = false
    }

    fun isTimerRunning(): Boolean{
        return job?.isActive ?: false
    }


    fun startTimer(){
        job = CoroutineScope(Dispatchers.Default).launchPeriodicAsync(secondInMillis){
            if(!isTimerPaused) {
                matchTimeInMillis.postValue(matchTimeInMillis.value?.plus(secondInMillis))
                val elapsedTime =
                    (matchTimeInMillis.value!! - ((intervals - intervalsLeft) * intervalDuration))
                val intervalTime = (intervalDuration) - secondInMillis
                if (elapsedTime >= intervalTime) {
                    intervalsLeft--
                    isMatchIntervalCompleted.postValue(true)
                    job?.cancel()
                }
            }
        }
    }



    fun stopTimer(){
        job?.cancel()
        resetTimer()
    }

    fun pauseTimer(){
        isTimerPaused = true
    }

    fun resumeTimer(){
        isTimerPaused = false
    }

    fun resetTimer(){
        this.intervalsLeft = 0
        this.isMatchIntervalCompleted.value = false
        this.intervalDuration = 0L
        this.matchTimeInMillis.value = 0L
        isTimerPaused = false
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

    fun setTimerValue(value: Float) {
        val currentIntervalMinTime = (intervals - intervalsLeft) * intervalDuration
        val currentIntervalMaxTime = (intervals - (intervalsLeft-1)) * intervalDuration
        if(value > currentIntervalMaxTime){
            intervalsLeft--
            setTimerValue(value)
        }else if(value < currentIntervalMinTime){
            intervalsLeft++
            setTimerValue(value)
        }else{
            matchTimeInMillis.value = value.toLong()
        }
    }


}