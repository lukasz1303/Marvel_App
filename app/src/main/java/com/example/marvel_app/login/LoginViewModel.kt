package com.example.marvel_app.login

import android.text.InputFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvel_app.UIState
import com.example.marvel_app.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :
    ViewModel() {

    private val _state = MutableLiveData<UIState>()
    val state: LiveData<UIState>
        get() = _state

    val emojiFilter =
        InputFilter { source, start, end, _, _, _ ->
            for (i in start until end) {
                val type = Character.getType(source[i])
                if (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt()) {
                    return@InputFilter ""
                }
            }
            null
        }

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
}