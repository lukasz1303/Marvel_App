package com.example.marvel_app.repository

import com.example.marvel_app.model.Comic
import com.example.marvel_app.network.MarvelApiService
import com.example.marvel_app.network.asDomainModel
import retrofit2.HttpException
import javax.inject.Inject

class ComicsRepository @Inject constructor(private val marvelApi: MarvelApiService) {

    suspend fun refreshComics(title: String?): List<Comic>? {
        val networkResponse =
            marvelApi.getResponseAsync(100, 0, "-onsaleDate", title)
        if (networkResponse.isSuccessful) {
            return networkResponse.body()?.data?.results?.asDomainModel()
        } else {
            throw HttpException(networkResponse)
        }
    }
}