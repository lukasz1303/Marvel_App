package com.example.marvel_app.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.marvel_app.repository.ComicsRepository
import kotlinx.coroutines.*
import java.lang.Exception

class HomeViewModel : ViewModel() {

    private val comicsRepository = ComicsRepository()

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val comics = comicsRepository.comics

    init {
        refreshComicsFromRepository()
    }

    private fun refreshComicsFromRepository(){
        coroutineScope.launch {
            try {
                comicsRepository.refreshComics()
                Log.i("HomeViewModel", "Success: comics refreshed")
            } catch (e: Exception){
                Log.i("HomeViewModel", "Failure: ${e.message}")
            }
        }
    }
}