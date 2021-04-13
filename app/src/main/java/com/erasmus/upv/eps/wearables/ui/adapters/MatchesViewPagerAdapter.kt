package com.erasmus.upv.eps.wearables.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.erasmus.upv.eps.wearables.ui.fragments.PastMatchesFragment
import com.erasmus.upv.eps.wearables.ui.fragments.UpcomingMatchesFragment

class MatchesViewPagerAdapter(fragment: Fragment)
    : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if(position == 0){
            UpcomingMatchesFragment.newInstance()
        }else{
            PastMatchesFragment.newInstance()
        }
//        val fragment = UpcomingMatchesFragment.newInstance()
//        fragment.arguments = Bundle().apply {
//            putString("TEST", "position $position")
//        }
//        return fragment
    }
}