package com.example.marvel_app.login

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
import com.example.marvel_app.UIState
import com.example.marvel_app.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var navController: NavController
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        binding.lifecycleOwner = this
        navController = this.findNavController()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSignUpButton()
        initSignInButton()
        initStateObserver()
    }

    private fun initSignUpButton() {
        binding.goToSignUpButton.setOnClickListener {
            navController
                .navigate(LoginFragmentDirections.actionLoginFragmentToSingUpFragment())
        }
    }

    private fun initSignInButton() {
        binding.signInButton.setOnClickListener {
            val email = binding.emailSignInEditText.editableText.toString()
            val password = binding.passwordSignInEditText.editableText.toString()
            activity?.let { viewModel.signInWithEmail(email, password) }
        }
    }

    private fun initStateObserver() {
        viewModel.state.observe(viewLifecycleOwner, {
            when (it) {
                is UIState.Success -> {
                    Toast.makeText(
                        activity, R.string.authentication_successful,
                        Toast.LENGTH_SHORT
                    ).show()
                    navController
                        .navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                }
                else -> {
                    Toast.makeText(
                        activity, R.string.authentication_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}