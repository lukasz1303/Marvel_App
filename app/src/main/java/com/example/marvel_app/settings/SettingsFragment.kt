package com.example.marvel_app.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.marvel_app.R
import com.example.marvel_app.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var navController: NavController
    private val viewModel: SettingViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        navController = this.findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLogoutButton()
    }

    private fun initLogoutButton() {
        binding.logoutButton.setOnClickListener {
            viewModel.signOut()
            Toast.makeText(
                activity, R.string.logout_successful,
                Toast.LENGTH_SHORT
            ).show()
            navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToLoginFragment())

        }
    }
}