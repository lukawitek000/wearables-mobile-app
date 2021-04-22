package com.erasmus.upv.eps.wearables.viewModels

import androidx.lifecycle.ViewModel
import com.erasmus.upv.eps.wearables.repositories.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateTeamViewModel
@Inject constructor(
    private val repository: TeamRepository
) : ViewModel(){

}