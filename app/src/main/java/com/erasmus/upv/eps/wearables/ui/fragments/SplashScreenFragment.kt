package com.erasmus.upv.eps.wearables.ui.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.erasmus.upv.eps.wearables.MainActivity
import com.erasmus.upv.eps.wearables.R

class SplashScreenFragment : Fragment() {


    companion object{
        const val IS_FIRST_LAUNCH_TAG = "First launch"
        private const val SPLASH_SCREEN_DELAY = 500L
    }

    private var isFirstLaunchOfApp = true



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)
        handleFirstRunOfApp()
        setHandlerForDelayAction()
        return view
    }


    private fun handleFirstRunOfApp() {
        val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        if(sharedPreferences.contains(IS_FIRST_LAUNCH_TAG)){
            isFirstLaunchOfApp = sharedPreferences.getBoolean(IS_FIRST_LAUNCH_TAG, false)
        }else{
            sharedPreferences.edit().putBoolean(IS_FIRST_LAUNCH_TAG, false).apply()
        }
    }

    private fun navigateAfterTimeout() {
        if(isFirstLaunchOfApp) {
            activity?.findNavController(R.id.nav_container)
                ?.navigate(R.id.action_splashScreenFragment_to_tutorialFragment)
        }else {
            activity?.findNavController(R.id.nav_container)
                ?.navigate(R.id.action_splashScreenFragment_to_matchesFragment)
        }
    }

    private fun setHandlerForDelayAction(){
        Handler(Looper.getMainLooper()).postDelayed(
            {
                navigateAfterTimeout()
            }, SPLASH_SCREEN_DELAY
        )
    }

}