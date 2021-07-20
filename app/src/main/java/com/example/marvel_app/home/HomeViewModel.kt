package com.example.marvel_app.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.marvel_app.UIState
import com.example.marvel_app.model.Comic
import com.example.marvel_app.repository.ComicsRepository
import com.example.marvel_app.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val comicsRepository: ComicsRepository,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _state = MutableLiveData<UIState>()
    val state: LiveData<UIState>
        get() = _state

    private val _searchingTitle = MutableLiveData<String>()
    val searchingTitle: LiveData<String>
        get() = _searchingTitle

    var currentSearchResult: Flow<PagingData<Comic>>? = null


    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.i("HomeViewModel", "Failure: ${exception.message}")
        _state.value = UIState.Error
    }

    private val mutableInSearching = MutableLiveData<Boolean>()
    val inSearching: LiveData<Boolean> get() = mutableInSearching

    fun setInSearching(inSearching: Boolean) {
        if (inSearching != mutableInSearching.value && inSearching) {
            viewModelScope.coroutineContext.cancelChildren()
            comics.value = listOf()
        } else if (mutableInSearching.value == true && !inSearching) {
            viewModelScope.coroutineContext.cancelChildren()
            refreshComicsFromRepository(null)
        }
        mutableInSearching.value = inSearching
    }

    fun refreshComicsFromRepositoryFlow(title: String?) : Flow<PagingData<Comic>>{
        val lastResult = currentSearchResult
        if (title == searchingTitle.value && lastResult != null){
            return lastResult
        }
        _searchingTitle.value = title
        val newResult: Flow<PagingData<Comic>> = comicsRepository.refreshComicsStream(title).cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    private val _navigateToSelectedComic = MutableLiveData<Comic>()
    val navigateToSelectedComic: LiveData<Comic>
        get() = _navigateToSelectedComic

    val comics = MutableLiveData<List<Comic>>()

    init {
        refreshComicsFromRepository(null)
        refreshComicsFromRepositoryFlow(null)
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
        _state.value = UIState.InSearching
    }

    fun checkIfUserSignedIn(): Boolean {
        firebaseRepository.user?.let {
            return true
        }
        return false
    }

    fun clearComicList() {
        comics.value = listOf()
    }
}
