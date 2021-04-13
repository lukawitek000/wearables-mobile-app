package com.erasmus.upv.eps.wearables

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpNavController()
        setUpActionBar()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setUpActionBar() {
        val appBarConfiguration = AppBarConfiguration
            .Builder(R.id.splashScreenFragment, R.id.tutorialFragment, R.id.matchesFragment)
            .build()
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
                R.id.splashScreenFragment -> {
                    supportActionBar?.hide()
                }
                else -> supportActionBar?.show()
            }
        }
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