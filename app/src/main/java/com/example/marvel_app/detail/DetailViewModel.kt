package com.example.marvel_app.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.model.Comic
import com.example.marvel_app.repository.ComicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val comicsRepository: ComicsRepository
) : ViewModel() {

    private val _selectedComic = MutableLiveData<Comic>()
    val selectedComic: LiveData<Comic>
        get() = _selectedComic

    fun setComic(comic: Comic) {
        _selectedComic.value = comic
    }

    fun insertComicToFavourites() {
        viewModelScope.launch {
            _selectedComic.value?.let {
                comicsRepository.insertComicToDatabase(it)
                _inFavourites.value = true
            }
        }
    }

    fun deleteComicFromFavourites() {
        viewModelScope.launch {
            _selectedComic.value?.let {
                comicsRepository.deleteComicFromDatabase(it)
                _inFavourites.value = false
            }
        }
    }

    private val _inFavourites = MutableLiveData<Boolean>()
    val inFavourites: LiveData<Boolean>
        get() = _inFavourites

    init {
        _inFavourites.value = false
    }


    fun checkIfComicInFavourites() {
        viewModelScope.launch {
            _selectedComic.value?.let {
                _inFavourites.value = comicsRepository.checkIfComicInDatabase(it.id)
            }
        }
    }

    fun handleStarIconClick(){
        if (_inFavourites.value == true) {
            deleteComicFromFavourites()
        } else {
            insertComicToFavourites()
        }
    }


}