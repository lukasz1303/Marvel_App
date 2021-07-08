package com.example.marvel_app.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvel_app.model.Comic

class DetailViewModel(comic: Comic) : ViewModel() {

    private val _selectedComic = MutableLiveData<Comic>()
    val selectedComic: LiveData<Comic>
        get() = _selectedComic

    init {
        _selectedComic.value = comic
    }
}