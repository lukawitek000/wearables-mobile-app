package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.ui.adapters.MatchesViewPagerAdapter
import com.erasmus.upv.eps.wearables.ui.fragments.MatchesFragment.Companion.MatchTime
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


//TODO show already recorded matches in past matches fragment

class MatchesViewPagerFragment : Fragment() {

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_view_pager_matches, container, false)

        viewPager = view.findViewById(R.id.matches_view_pager)
        viewPager.adapter = MatchesViewPagerAdapter(this)

        view.findViewById<FloatingActionButton>(R.id.create_match_fb).setOnClickListener {
            findNavController().navigate(R.id.action_matchesFragment_to_createMatchFragment)
        }

        setUpTabLayout(view)


        return view
    }

    private fun setUpTabLayout(view: View) {
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) {
                tab, position ->
            if(position == 0){
                tab.text = MatchTime.UPCOMING.name
            }else{
                tab.text = MatchTime.PAST.name
            }
        }.attach()
    }


}



