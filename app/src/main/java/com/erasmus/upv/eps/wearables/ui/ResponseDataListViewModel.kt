package com.erasmus.upv.eps.wearables.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erasmus.upv.eps.wearables.model.Response
import kotlin.random.Random

class ResponseDataListViewModel : ViewModel() {

/*
    val data: MutableList<Response> = mutableListOf(
        Response(234423423423L, 143),
        Response(98089678L, 1),
        Response(423L, 3)
    )*/


    val data: MutableList<Response> = mutableListOf()

    var responseData = MutableLiveData<MutableList<Response>>()

    init {
        responseData.value = emptyList<Response>().toMutableList()
    }

    fun addResponse(){
        val randomTimeStamp = Random.nextLong(0L, 1000000000000000L)
        val randomData = Random.nextInt(0, 200)
        //data.add(Response(randomTimeStamp, randomData))
    }




}