package com.example.marvel_app.network

data class NetworkData(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<NetworkComic>
)
