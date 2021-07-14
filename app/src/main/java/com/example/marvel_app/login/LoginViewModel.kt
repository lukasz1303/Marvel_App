package com.example.marvel_app.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvel_app.UIState
import com.example.marvel_app.repository.FirebaseRepository

class LoginViewModel : ViewModel() {

    private val firebaseRepository = FirebaseRepository()

    private val _state = MutableLiveData<UIState>()
    val state: LiveData<UIState>
        get() = _state


    fun signInWithEmail(email: String, password: String) {
        _state.value = UIState.InProgress
        val result = firebaseRepository.signInWithEmail(email, password)
        result.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _state.value = UIState.Success
            } else {
                _state.value = UIState.Error
            }
        }
    }

    fun signUpWithEmail(email: String, password: String) {
        _state.value = UIState.InProgress
        val result = firebaseRepository.signUpWithEmail(email, password)
        result.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _state.value = UIState.Success
            } else {
                _state.value = UIState.Error
            }
        }
    }

    fun checkIfUserSignedIn(): Boolean {
        firebaseRepository.user?.let {
            return true
        }
        return false
    }
}