package com.erasmus.upv.eps.wearables.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.repositories.MatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.security.interfaces.RSAMultiPrimePrivateCrtKey
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateMatchViewModel
    @Inject constructor(private val repository: MatchRepository): ViewModel() {


    var matchDate: Calendar = Calendar.getInstance()

    fun createMatch(match: Match){
        Log.i("CreateMatchViewModel", "createMatch: $match")
        viewModelScope.launch {
            match.date = Date(matchDate.timeInMillis)
            repository.insertMatch(match)
        }
    }
    

}