package com.erasmus.upv.eps.wearables.viewModels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.repositories.MatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MatchesViewModel
@Inject constructor(
    private val repository: MatchRepository
) : ViewModel(){

    fun getAllUpcomingMatches(): LiveData<List<Match>>{
        return repository.allMatches.asLiveData()
    }

    fun getAllPastMatches(): LiveData<List<Match>>{
        return repository.allMatches.asLiveData()
    }

    fun getInfoAboutTheMatch(id: Long): LiveData<Match>{
        return repository.getMatchById(id)
    }


}