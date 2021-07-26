package com.example.marvel_app

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.marvel_app.databinding.ActivityMainBinding
import com.example.marvel_app.detail.DetailFragmentDirections
import com.example.marvel_app.favourites.FavouritesFragmentDirections
import com.example.marvel_app.home.HomeFragmentDirections
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
        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.nav_host_fragment)

        checkIfSignedIn()
        setTheme()
        setupBottomNavigationView()
        setupActionBar()
        setupNavController()
    }

    private fun setupNavController() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    setupLargeToolbar()
                }
                R.id.loginFragment -> {
                    setupSmallToolbar()
                }
                R.id.signUpFragment -> {
                    setupSmallToolbar()
                }
                R.id.detailFragment -> {
                    setupSmallToolbar()
                }
                R.id.settingsFragment -> {
                    setupSmallToolbar()
                }
                R.id.favouritesFragment -> {
                    setupSmallToolbar()
                }
            }
        }
    }

    private fun setupSmallToolbar() {
        binding.bottomNavigation.visibility = View.VISIBLE
        binding.toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText_Small)
        binding.toolbar.titleMarginTop = 0
        setSupportActionBar(binding.toolbar)
    }

    private fun setupLargeToolbar() {
        binding.bottomNavigation.visibility = View.VISIBLE
        binding.toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText)
        binding.toolbar.titleMarginTop =
            resources.getDimension(R.dimen.home_toolbar_title_margin_top).toInt()
        setSupportActionBar(binding.toolbar)
    }

    private fun setupActionBar() {
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginFragment,
                R.id.homeFragment,
                R.id.favouritesFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }


    private fun setupBottomNavigationView() {

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (navController.currentDestination?.id) {
                R.id.detailFragment -> navController.navigate(DetailFragmentDirections.actionDetailFragmentToHomeFragment())
                R.id.settingsFragment -> navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToHomeFragment())
                R.id.favouritesFragment -> navController.navigate(FavouritesFragmentDirections.actionFavouritesFragmentToHomeFragment())
            }
            viewModel.setSearchingTitle(null)
            viewModel.clearComicsList()
            when (item.itemId) {
                R.id.home_bottom_navigation -> {
                    viewModel.setInSearching(false)
                    true
                }
                R.id.search_bottom_navigation -> {
                    viewModel.setInSearching(true)
                    true
                }
                R.id.favourites_bottom_navigation -> {
                    navController.navigate(HomeFragmentDirections.actionHomeFragmentToFavouritesFragment())
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

    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.homeFragment -> {
                if (viewModel.inSearching.value == true) {
                    viewModel.setInSearching(false)
                    binding.bottomNavigation.selectedItemId = R.id.home_bottom_navigation
                }
            }
            R.id.favouritesFragment -> {
                navController.navigate(FavouritesFragmentDirections.actionFavouritesFragmentToHomeFragment())
                binding.bottomNavigation.selectedItemId = R.id.home_bottom_navigation
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    private fun setTheme() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val defaultValue = false
        val darkTheme =
            sharedPref.getBoolean(getString(R.string.preference_dark_theme), defaultValue)
        AppCompatDelegate.setDefaultNightMode(if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }
}