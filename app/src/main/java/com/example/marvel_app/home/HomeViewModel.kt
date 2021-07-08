package com.example.marvel_app.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.UIState
import com.example.marvel_app.model.Comic
import com.example.marvel_app.repository.ComicsRepository
import kotlinx.coroutines.*

class HomeViewModel : ViewModel() {

    private val comicsRepository = ComicsRepository()
    private val _state = MutableLiveData<UIState>()
    val state: LiveData<UIState>
        get() = _state


    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.i("HomeViewModel", "Failure: ${exception.message}")
        _state.value = UIState.Error
    }

    private val _navigateToSelectedComic = MutableLiveData<Comic>()
    val navigateToSelectedComic: LiveData<Comic>
        get() = _navigateToSelectedComic

    val comics = MutableLiveData<List<Comic>>()

    init {
        refreshComicsFromRepository()
    }

    private fun refreshComicsFromRepository() {
        _state.value = UIState.InProgress
        viewModelScope.launch(exceptionHandler) {
            comics.value = comicsRepository.refreshComics()
            _state.value = UIState.Success
        }
    }

    fun displayComicDetail(comic: Comic){
        _navigateToSelectedComic.value = comic
    }

    fun displayComicDetailComplete(){
        _navigateToSelectedComic.value = null
    }
}
