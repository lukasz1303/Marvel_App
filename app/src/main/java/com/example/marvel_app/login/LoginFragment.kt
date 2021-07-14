package com.example.marvel_app.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
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
        navController = findNavController()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkIfSignedIn()
        initSignUpButton()
        initSignInButton()
        initStateObserver()
    }

    private fun checkIfSignedIn() {
        if (viewModel.checkIfUserSignedIn()) {
            navController
                .navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
        }
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
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    activity, R.string.provide_email_and_password,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                activity?.let { viewModel.signInWithEmail(email, password) }
            }
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }

    private fun initStateObserver() {
        viewModel.state.observe(viewLifecycleOwner, {
            when (it) {
                is UIState.Success -> {
                    binding.loginProgressBar.visibility = View.GONE
                    Toast.makeText(
                        activity, R.string.authentication_successful,
                        Toast.LENGTH_SHORT
                    ).show()
                    navController
                        .navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                }
                is UIState.InProgress -> {
                    binding.loginProgressBar.visibility = View.VISIBLE
                }
                else -> {
                    binding.loginProgressBar.visibility = View.GONE

                    Toast.makeText(
                        activity, R.string.authentication_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}