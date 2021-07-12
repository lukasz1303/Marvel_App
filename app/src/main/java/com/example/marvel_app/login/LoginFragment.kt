package com.example.marvel_app.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.marvel_app.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        binding.lifecycleOwner = this
        auth = Firebase.auth
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSignUpButton()
        initSignInButton()
    }

    private fun initSignUpButton() {
        binding.goToSignUpButton.setOnClickListener {
            this.findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToSingUpFragment())
        }
    }

    private fun initSignInButton(){
        binding.signInButton.setOnClickListener {
            val email = binding.emailSignInEditText.editableText.toString()
            val password = binding.passwordSignInEditText.editableText.toString()
            signIn(email, password)
        }
    }

    private fun signIn(email: String, password: String) {
        activity?.let {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            activity, "Authentication successful.",
                            Toast.LENGTH_SHORT
                        ).show()
                        this.findNavController()
                            .navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    } else {
                        Toast.makeText(
                            activity, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

}