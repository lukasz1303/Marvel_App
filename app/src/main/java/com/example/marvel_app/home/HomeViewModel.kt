package com.example.marvel_app.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {

    private val _response = MutableLiveData<String>()
    val response : LiveData<String>
    get() = _response

    init {
        _response.value = "Test result"
    }
}