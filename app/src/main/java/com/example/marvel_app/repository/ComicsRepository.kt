package com.example.marvel_app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.marvel_app.database.ComicsDatabase
import com.example.marvel_app.database.DatabaseComic
import com.example.marvel_app.database.asDomainModel
import com.example.marvel_app.model.Comic
import com.example.marvel_app.network.MarvelApiService
import com.example.marvel_app.pagination.ComicsPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ComicsRepository @Inject constructor(
    private val marvelApi: MarvelApiService,
    private val database: ComicsDatabase
) {

    fun refreshComicsStream(title: String?): Flow<PagingData<Comic>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ComicsPagingSource(marvelApi, title) }
        ).flow
    }

    fun getComicsFromDatabase(): LiveData<List<Comic>> {
        return Transformations.map(database.comicDao.getAll()) {
            it.asDomainModel()
        }
    }

    fun insertComicToDatabase(comic: Comic) {
        database.comicDao.insertAll(
            DatabaseComic(
                comic.id,
                comic.title,
                comic.imageUrl,
                comic.imageExtension,
                comic.description,
                comic.authors,
                comic.detailUrl
            )
        )
    }


    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }

}