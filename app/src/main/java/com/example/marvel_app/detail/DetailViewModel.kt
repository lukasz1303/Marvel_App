package com.example.marvel_app.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvel_app.model.Comic
import com.example.marvel_app.repository.ComicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val comicsRepository: ComicsRepository) : ViewModel() {

    private val _selectedComic = MutableLiveData<Comic>()
    val selectedComic: LiveData<Comic>
        get() = _selectedComic

    fun setComic(comic: Comic){
        _selectedComic.value = comic
    }
}