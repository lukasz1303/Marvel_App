package com.example.marvel_app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comic(
    val title: String,
    val imageUrl: String?,
    val imageExtension: String?,
    val description: String?,
    val authors: List<String>?
) :Parcelable