package com.example.marvel_app.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvel_app.UIState
import com.example.marvel_app.repository.FirebaseRepository

class SettingViewModel: ViewModel() {

    private val firebaseRepository = FirebaseRepository()

    fun signOut() {
        firebaseRepository.signOut()
    }
}