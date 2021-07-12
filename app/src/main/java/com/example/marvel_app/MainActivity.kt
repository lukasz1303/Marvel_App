package com.example.marvel_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.marvel_app.home.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
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

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment ){
                bottomNavigationView.visibility = View.VISIBLE
            }
            if (destination.id == R.id.loginFragment ){
                bottomNavigationView.visibility = View.GONE
            }
        }
    }
}