package com.erasmus.upv.eps.wearables.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.erasmus.upv.eps.wearables.ui.fragments.MatchesFragment
import com.erasmus.upv.eps.wearables.ui.fragments.MatchesFragment.Companion.MatchTime

class MatchesViewPagerAdapter(fragment: Fragment)
    : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = MatchesFragment.newInstance()
        fragment.arguments = Bundle().apply {
            if(position == 0){
                putSerializable(MatchesFragment.MATCH_TIME_KEY, MatchTime.UPCOMING)
            }else{
                putSerializable(MatchesFragment.MATCH_TIME_KEY, MatchTime.PAST)
            }

        }
        return fragment
    }
}