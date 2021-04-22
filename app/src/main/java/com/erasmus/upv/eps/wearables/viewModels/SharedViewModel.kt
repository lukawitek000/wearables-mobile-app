package com.erasmus.upv.eps.wearables.viewModels

import androidx.lifecycle.ViewModel
import com.erasmus.upv.eps.wearables.model.Match
import com.erasmus.upv.eps.wearables.model.Team
import dagger.hilt.android.lifecycle.HiltViewModel


class SharedViewModel : ViewModel() {


    var match: Match? = null
    var homeTeam: Team? = null
    var guestTeam: Team? = null
}