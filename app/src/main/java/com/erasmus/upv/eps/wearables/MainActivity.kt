package com.erasmus.upv.eps.wearables

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.erasmus.upv.eps.wearables.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var bottomNavigation: BottomNavigationView


    companion object{
        const val IS_FIRST_LAUNCH_TAG = "First launch"
    }

    private var isFirstLaunchOfApp = true


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Wearables)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setUpNavController()
        setUpActionBar()
        setUpBottomNavigation()
        checkFirstRunOfTheApp()
        navigateToMatchesFragment()
    }



    private fun setUpBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation.setupWithNavController(navController)
        controlBottomNavigationVisibility()
    }

    private fun setUpActionBar() {
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.tutorialFragment,
                R.id.matchesFragment,
                R.id.teamsFragment,
                R.id.playersFragment,
                R.id.infoFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setUpNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_container) as NavHostFragment
        navController = navHostFragment.navController
    }


    private fun controlBottomNavigationVisibility(){
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.tutorialFragment -> setBottomNavigationVisibility(View.GONE)
                R.id.createMatchFragment -> setBottomNavigationVisibility(View.GONE)
                R.id.createTeamFragment -> setBottomNavigationVisibility(View.GONE)
                R.id.createPlayerFragment -> setBottomNavigationVisibility(View.GONE)
                else -> setBottomNavigationVisibility(View.VISIBLE)
            }
        }
    }


    fun setBottomNavigationVisibility(visibility: Int){
        bottomNavigation.visibility = visibility
    }



    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }



    private fun checkFirstRunOfTheApp() {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE) ?: return
        if(sharedPreferences.contains(IS_FIRST_LAUNCH_TAG)){
            isFirstLaunchOfApp = sharedPreferences.getBoolean(IS_FIRST_LAUNCH_TAG, false)
        }else{
            sharedPreferences.edit().putBoolean(IS_FIRST_LAUNCH_TAG, false).apply()
        }
    }



    private fun navigateToMatchesFragment() {
        if(!isFirstLaunchOfApp){
            navController.navigate(R.id.action_tutorialFragment_to_matchesFragment)
        }
    }



}