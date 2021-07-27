package com.example.marvel_app.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.marvel_app.database.*
import com.example.marvel_app.model.Comic
import com.example.marvel_app.network.MarvelApiService
import com.example.marvel_app.pagination.ComicsPagingSource
import kotlinx.coroutines.*
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

    suspend fun getComicsFromDatabase(): List<Comic> {
        return database.comicDao.getAll().asDomainModel()
    }

    suspend fun insertComicToDatabase(comic: Comic) {
        database.comicDao.insertComic(
            DatabaseComic(
                comic.id,
                comic.title,
                comic.imageUrl,
                comic.imageExtension,
                comic.description,
                comic.detailUrl
            )
        )
        val databaseAuthors = comic.authors?.map {
            Author(
                name = it,
                comicId = comic.id
            )
        }
        if (databaseAuthors != null) {
            database.comicDao.insertAuthors(databaseAuthors)
        }

    }

    suspend fun deleteComicFromDatabase(comic: Comic) {
        withContext(Dispatchers.IO) {
            database.comicDao.deleteComic(
                DatabaseComic(
                    comic.id,
                    comic.title,
                    comic.imageUrl,
                    comic.imageExtension,
                    comic.description,
                    comic.detailUrl
                )
            )
            val databaseAuthors = comic.authors?.map {
                Author(
                    name = it,
                    comicId = comic.id
                )
            }
            if (databaseAuthors != null) {
                database.comicDao.deleteAuthors(databaseAuthors)
            }
        }
    }

    suspend fun checkIfComicInDatabase(id: Int): Boolean {
        var result: DatabaseComicWithAuthors? = null
        result = database.comicDao.getComic(id)
        result?.let {
            return true
        }
        return false
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }

}