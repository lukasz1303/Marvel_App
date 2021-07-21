package com.example.marvel_app.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.marvel_app.model.Comic
import com.example.marvel_app.network.MarvelApiService
import com.example.marvel_app.network.asDomainModel
import com.example.marvel_app.repository.ComicsRepository.Companion.NETWORK_PAGE_SIZE
import retrofit2.HttpException
import java.io.IOException

class ComicsPagingSource(private val service: MarvelApiService, private val query: String?) :
    PagingSource<Int, Comic>() {
    override fun getRefreshKey(state: PagingState<Int, Comic>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comic> {
        val position = params.key ?: 0
        val apiQuery = query
        return try {
            val response = service.getResponseAsync(100, position, "-onsaleDate", apiQuery)
            val comics = response.body()?.data?.results?.asDomainModel() ?: listOf()
            val nextKey = if (comics.isEmpty()) {
                null
            } else {
                position + (params.loadSize / 100)
            }
            LoadResult.Page(
                data = comics,
                prevKey = if (position == 0) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }

    }
}