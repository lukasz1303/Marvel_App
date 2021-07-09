package com.example.marvel_app.repository

import com.example.marvel_app.model.Comic
import com.example.marvel_app.network.MarvelApi
import com.example.marvel_app.network.asDomainModel
import retrofit2.HttpException

class ComicsRepository {

    suspend fun refreshComics(title: String?): List<Comic>? {
        val networkResponse =
            MarvelApi.retrofitServiceMarvel.getResponseAsync(100, 0, "-onsaleDate",title)
        if (networkResponse.isSuccessful) {
            return networkResponse.body()?.data?.results?.asDomainModel()
        } else {
            throw HttpException(networkResponse)
        }
    }
}