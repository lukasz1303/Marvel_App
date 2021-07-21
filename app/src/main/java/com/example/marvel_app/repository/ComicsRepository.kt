package com.example.marvel_app.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.marvel_app.model.Comic
import com.example.marvel_app.network.MarvelApiService
import com.example.marvel_app.pagination.ComicsPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ComicsRepository @Inject constructor(private val marvelApi: MarvelApiService) {

    fun refreshComicsStream(title: String?): Flow<PagingData<Comic>> {
        return Pager(
            config = PagingConfig(
                pageSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ComicsPagingSource(marvelApi, title) }
        ).flow
    }

}