package com.example.marvel_app.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.marvel_app.R
import com.example.marvel_app.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSignUpBinding

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
    }

    private fun initSignUpButton() {
        binding.signUpButton.setOnClickListener {
            val email = binding.emailSignUpEditText.editableText.toString()
            val password = binding.passwordSignUpEditText.editableText.toString()
            createAccount(email, password)
        }
    }

    private fun createAccount(email: String, password: String) {
        activity?.let {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, R.string.sign_up_successful, Toast.LENGTH_SHORT)
                            .show()
                        val user = auth.currentUser
                    } else {
                        Toast.makeText(activity, R.string.sign_up_failure, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }
}