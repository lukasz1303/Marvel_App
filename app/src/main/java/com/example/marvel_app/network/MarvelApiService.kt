package com.example.marvel_app.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

private const val BASE_URL = "https://gateway.marvel.com/v1/public/"
private const val apikey = "080a502746c8a60aeab043387a56eef0"
private const val hash = "6edc18ab1a954d230c1f03c590d469d2"
private const val ts = "1"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface MarvelApiService {
    @GET("comics?ts=$ts&apikey=$apikey&hash=$hash")
    suspend fun getResponseAsync(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("orderBy") orderBy: String,
        @Query("titleStartsWith") title: String?
    ): Response<NetworkResponse>
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object MarvelModule {
    @Provides
    fun provideMarvelService(): MarvelApiService = retrofit.create(MarvelApiService::class.java)
}