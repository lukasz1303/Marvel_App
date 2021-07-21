package com.example.marvel_app.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.marvel_app.model.Comic
import com.example.marvel_app.network.MarvelApiService
import com.example.marvel_app.network.asDomainModel
import com.example.marvel_app.pagination.ComicsPagingSource
import kotlinx.coroutines.flow.Flow
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

    fun refreshComicsStream(title: String?): Flow<PagingData<Comic>> {
        return Pager(
            config = PagingConfig(
                pageSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {ComicsPagingSource(marvelApi, title)}
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 100
    }

}