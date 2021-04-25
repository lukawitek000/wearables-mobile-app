package com.erasmus.upv.eps.wearables

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var navController: NavController
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        setUpNavController()
        setUpActionBar()
        setUpBottomNavigation()


    }


    private fun setUpBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation.setupWithNavController(navController)
        controlBottomNavigationVisibility()
    }

    private fun setUpActionBar() {
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.splashScreenFragment,
                R.id.tutorialFragment,
                R.id.matchesFragment,
                R.id.teamsFragment,
                R.id.playersFragment,
                R.id.infoFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        controlActionBarVisibility()
    }

    private fun setUpNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_container) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun controlActionBarVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashScreenFragment -> supportActionBar?.hide()
                else -> supportActionBar?.show()
            }
        }
    }


    private fun controlBottomNavigationVisibility(){
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashScreenFragment -> setBottomNavigationVisibility(View.GONE)
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





}