package com.example.marvel_app.model

data class Comic(
    val title: String,
    val imageUrl: String?,
    val description: String?,
    val authors: List<String>?
)