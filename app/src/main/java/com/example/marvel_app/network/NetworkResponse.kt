package com.example.marvel_app.network

data class NetworkResponse(
    val code: Int,
    val status: String,
    val data: NetworkData
)