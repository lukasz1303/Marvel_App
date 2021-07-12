package com.example.marvel_app.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.marvel_app.R
import com.example.marvel_app.UIState
import com.example.marvel_app.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: LoginViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater)
        binding.lifecycleOwner = this
        auth = Firebase.auth
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
            activity?.let { it1 -> viewModel.signUpWithEmail(it1, email, password) }
        }
    }

    private fun initStateObserver() {
        viewModel.state.observe(viewLifecycleOwner, {
            when (it) {
                is UIState.Success -> {
                    Toast.makeText(
                        activity, R.string.sign_up_successful,
                        Toast.LENGTH_SHORT
                    ).show()
                    activity?.onBackPressed()
                }
                else -> {
                    Toast.makeText(
                        activity, R.string.sign_up_failure,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}