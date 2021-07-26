package com.example.marvel_app.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.UIState
import com.example.marvel_app.model.Comic
import com.example.marvel_app.repository.ComicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val comicsRepository: ComicsRepository,
) : ViewModel() {

    private val _state = MutableLiveData<UIState>()
    val state: LiveData<UIState>
        get() = _state

    private val _navigateToSelectedComic = MutableLiveData<Comic>()
    val navigateToSelectedComic: LiveData<Comic>
        get() = _navigateToSelectedComic

    val comics = MutableLiveData<List<Comic>>()

    init {
        refreshFavouritesComicsFromRepository()
    }

    fun refreshFavouritesComicsFromRepository() {
        _state.value = UIState.InProgress
        viewModelScope.launch {
            comics.value = comicsRepository.getComicsFromDatabase()
            if (comics.value.isNullOrEmpty()) {
                _state.value = UIState.Error
            } else {
                _state.value = UIState.Success
            }
        }
    }

    fun displayComicDetail(comic: Comic) {
        _navigateToSelectedComic.value = comic
    }

    fun displayComicDetailComplete() {
        _navigateToSelectedComic.value = null
    }

}