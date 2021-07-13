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
import com.example.marvel_app.databinding.FragmentSignUpBinding
import java.lang.Exception


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
        navController = this.findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSignUpButton()
        initStateObserver()
    }

    private fun initSignUpButton() {
        binding.signUpButton.setOnClickListener {
            val email = binding.emailSignUpEditText.editableText.toString()
            val password = binding.passwordSignUpEditText.editableText.toString()
            viewModel.signUpWithEmail(email, password)
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