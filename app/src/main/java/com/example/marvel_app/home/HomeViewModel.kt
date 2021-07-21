package com.example.marvel_app.home

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

    private val mutableInSearching = MutableLiveData<Boolean>()
    val inSearching: LiveData<Boolean> get() = mutableInSearching

    fun setInSearching(inSearching: Boolean) {
        mutableInSearching.value = inSearching
    }

    fun refreshComicsFromRepositoryFlow(title: String?): Flow<PagingData<Comic>> {

        _searchingTitle.value = title
        val newResult: Flow<PagingData<Comic>> =
            comicsRepository.refreshComicsStream(title).cachedIn(viewModelScope)
        return newResult
    }

    private val _navigateToSelectedComic = MutableLiveData<Comic>()
    val navigateToSelectedComic: LiveData<Comic>
        get() = _navigateToSelectedComic

    init {
        refreshComicsFromRepositoryFlow(null)
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

    fun changeState(state: UIState) {
        _state.value = state
    }
}
