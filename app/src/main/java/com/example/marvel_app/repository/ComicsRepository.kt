package com.example.marvel_app.repository

import androidx.lifecycle.MutableLiveData
import com.example.marvel_app.model.Comic
import com.example.marvel_app.network.MarvelApi
import com.example.marvel_app.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

class ComicsRepository {

    suspend fun refreshComics(): List<Comic>? {
        val networkResponse =
            MarvelApi.retrofitServiceMarvel.getResponseAsync(100, 0, "-onsaleDate")
        if(networkResponse.isSuccessful) {
            return networkResponse.body()?.data?.results?.asDomainModel()
        }
        else {
            throw HttpException(networkResponse)
        }
    }
}