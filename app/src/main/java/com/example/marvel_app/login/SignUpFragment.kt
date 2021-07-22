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
import com.example.marvel_app.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater)
        binding.lifecycleOwner = this
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSignUpButton()
        initStateObserver()
        setEmailEmojiFilter()
    }

    private fun setEmailEmojiFilter() {
        binding.emailSignUpEditText.filters = arrayOf(viewModel.emojiFilter)
    }

    private fun initSignUpButton() {
        binding.signUpButton.setOnClickListener {
            val email = binding.emailSignUpEditText.editableText.toString()
            val password = binding.passwordSignUpEditText.editableText.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    activity, R.string.provide_email_and_password,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                tryToSignUp(email,password)
            }
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }

    private fun tryToSignUp(email: String, password: String) {
        if (viewModel.isEmailValid(email)) {
            viewModel.signUpWithEmail(email, password)
        } else {
            Toast.makeText(
                activity, R.string.invalid_email_error_msg,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initStateObserver() {
        viewModel.state.observe(viewLifecycleOwner, {
            when (it) {
                is UIState.Success -> {
                    binding.signUpProgressBar.visibility = View.GONE

                    Toast.makeText(
                        activity, R.string.sign_up_successful,
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigateUp()
                }
                is UIState.InProgress -> {
                    binding.signUpProgressBar.visibility = View.VISIBLE
                }
                else -> {
                    binding.signUpProgressBar.visibility = View.GONE

                    Toast.makeText(
                        activity, R.string.sign_up_failure,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}