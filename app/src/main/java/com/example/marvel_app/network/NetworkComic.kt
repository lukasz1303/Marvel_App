package com.example.marvel_app.network

data class NetworkComic(
    val id: Int,
    val title: String,
    val description: String?,
    val urls: List<NetworkComicUrl?>?,
    val thumbnail: NetworkThumbnail?,
    val creators: NetworkCreators
)