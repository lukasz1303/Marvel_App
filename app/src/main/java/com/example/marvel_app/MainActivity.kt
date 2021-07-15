package com.example.marvel_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.marvel_app.databinding.ActivityMainBinding
import com.example.marvel_app.home.HomeViewModel

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

        checkIfSignedIn(savedInstanceState)
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

    private fun checkIfSignedIn(bundle: Bundle?) {
        if (viewModel.checkIfUserSignedIn()) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true)
                .build()
            findNavController(R.id.nav_host_fragment).navigate(
                R.id.action_loginFragment_to_homeFragment,
                bundle,
                navOptions
            );
        }
    }
}