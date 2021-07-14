package com.example.marvel_app.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel_app.UIState
import com.example.marvel_app.model.Comic
import com.example.marvel_app.repository.ComicsRepository
import com.example.marvel_app.repository.FirebaseRepository
import kotlinx.coroutines.*

class HomeViewModel : ViewModel() {

    private val comicsRepository = ComicsRepository()
    private val firebaseRepository = FirebaseRepository()

    private val _state = MutableLiveData<UIState>()
    val state: LiveData<UIState>
        get() = _state

    fun changeState(state: UIState) {
        _state.value = state
    }


    private val _searchingTitle = MutableLiveData<String>()
    val searchingTitle: LiveData<String>
        get() = _searchingTitle


    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.i("HomeViewModel", "Failure: ${exception.message}")
        _state.value = UIState.Error
    }

    private val mutableInSearching = MutableLiveData<Boolean>()
    val inSearching: LiveData<Boolean> get() = mutableInSearching

    fun setInSearching(inSearching: Boolean) {
        viewModelScope.coroutineContext.cancelChildren()
        mutableInSearching.value = inSearching
    }

    private val _navigateToSelectedComic = MutableLiveData<Comic>()
    val navigateToSelectedComic: LiveData<Comic>
        get() = _navigateToSelectedComic

    val comics = MutableLiveData<List<Comic>>()

    init {
        refreshComicsFromRepository(null)
    }

    fun refreshComicsFromRepository(title: String?) {
        comics.value = listOf()
        _state.value = UIState.InProgress
        _searchingTitle.value = title
        viewModelScope.launch(exceptionHandler) {
            comics.value = comicsRepository.refreshComics(title)
            if (comics.value?.isEmpty() == true && title != null) {
                _state.value = UIState.SearchingError
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

    fun initFragmentForSearching() {
        comics.value = listOf()
        changeState(UIState.InSearching)
    }

    fun checkIfUserSignedIn(): Boolean {
        firebaseRepository.user?.let {
            return true
        }
        return false
    }
}
