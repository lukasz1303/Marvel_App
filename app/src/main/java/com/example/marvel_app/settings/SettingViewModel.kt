package com.example.marvel_app.settings

import androidx.lifecycle.ViewModel
import com.example.marvel_app.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :
    ViewModel() {

    fun signOut() {
        firebaseRepository.signOut()
    }
}