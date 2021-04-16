package com.example.weatherapp.ui.activities

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.util.ContextUtils
import com.example.weatherapp.util.ContextUtils.Companion.updateLocalization
import com.example.weatherapp.util.Setting
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
       val appBarConfiguration = AppBarConfiguration(setOf(
               R.id.navigation_home, R.id.navigation_favourite, R.id.navigation_settings, R.id.navigation_alerts))
        //navView.setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        //binding= ActivityMainBinding.inflate(layoutInflater)

        //navController = Navigation.findNavController(this, R.id.nav_host_fragment)

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,null)
    }
    override fun attachBaseContext(newBase: Context?) {

        val sp = PreferenceManager.getDefaultSharedPreferences(newBase)
        val lang = sp.getString("LANGUAGE_SYSTEM", Locale.getDefault().language)
        updateLocalization(newBase!!, Locale(lang!!))
        super.attachBaseContext(newBase)
    }
    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.navView.setupWithNavController(navController)
        binding.navView.setOnNavigationItemReselectedListener {
            it.setChecked(true)

            when (it.getItemId()) {
                R.id.navigation_home -> navController.navigate(R.id.navigation_home)
                R.id.navigation_favourite -> navController.navigate(R.id.navigation_favourite)
                R.id.navigation_settings -> navController.navigate(R.id.navigation_settings)
                R.id.navigation_alerts -> navController.navigate(R.id.navigation_alerts)
            }
            //binding.drawerLayout.closeDrawers()

            true
        }
    }
}