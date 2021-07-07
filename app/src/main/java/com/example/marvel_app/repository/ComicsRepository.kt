package com.example.marvel_app.repository

import androidx.lifecycle.MutableLiveData
import com.example.marvel_app.model.Comic
import com.example.marvel_app.network.MarvelApi
import com.example.marvel_app.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ComicsRepository {

    val comics = MutableLiveData<List<Comic>>()
    suspend fun refreshComics() {
        withContext(Dispatchers.IO){}
        val networkResponse = MarvelApi.retrofitServiceMarvel.getResponse(100,0,"-onsaleDate").await()
        comics.value = networkResponse.data.results.asDomainModel()
    }
}