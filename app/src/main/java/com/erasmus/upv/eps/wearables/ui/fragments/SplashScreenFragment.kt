package com.erasmus.upv.eps.wearables.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.erasmus.upv.eps.wearables.MainActivity
import com.erasmus.upv.eps.wearables.R

class SplashScreenFragment : Fragment() {


    private val SPLASH_SCREEN_DELAY = 500L
    private val isFirstLaunchOfApp = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.splash_screen_fragment, container, false)

        navigateAfterTimeout()

        return view
    }

    private fun navigateAfterTimeout() {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                if(isFirstLaunchOfApp) {
                    activity?.findNavController(R.id.nav_container)
                        ?.navigate(R.id.action_splashScreenFragment_to_tutorialFragment)
                }else {
                    activity?.findNavController(R.id.nav_container)
                        ?.navigate(R.id.action_splashScreenFragment_to_matchesFragment)
                }
            }, SPLASH_SCREEN_DELAY
        )
    }

}