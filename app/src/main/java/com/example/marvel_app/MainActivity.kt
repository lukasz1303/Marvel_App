package com.example.marvel_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.marvel_app.databinding.ActivityMainBinding
import com.example.marvel_app.detail.DetailFragmentDirections
import com.example.marvel_app.home.HomeViewModel
import com.example.marvel_app.settings.SettingsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Marvel_App)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment)

        checkIfSignedIn()
        setupBottomNavigationView()
        setupActionBar()
        setupNavController()
    }

    private fun setupNavController() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment) {
                binding.bottomNavigation.visibility = View.VISIBLE
            }
            if (destination.id == R.id.loginFragment) {
                binding.bottomNavigation.visibility = View.GONE
            }
        }
    }

    private fun setupActionBar() {
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.loginFragment, R.id.homeFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }


    private fun setupBottomNavigationView() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->

            when (navController.currentDestination?.id) {
                R.id.detailFragment -> navController.navigate(DetailFragmentDirections.actionDetailFragmentToHomeFragment())
                R.id.settingsFragment -> navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToHomeFragment())
            }

            when (item.itemId) {
                R.id.home_bottom_navigation -> {
                        viewModel.setInSearching(false)
                    true
                }
                R.id.search_bottom_navigation -> {
                        viewModel.setInSearching(true)
                    true
                }
                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
                || super.onSupportNavigateUp()
    }

    private fun checkIfSignedIn() {
        if (viewModel.checkIfUserSignedIn() && navController.currentDestination?.id == R.id.loginFragment) {
            findNavController(R.id.nav_host_fragment).navigate(
                R.id.action_loginFragment_to_homeFragment
            )
        }
    }
}