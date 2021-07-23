package com.example.marvel_app.model

import android.os.Parcelable
import com.example.marvel_app.database.DatabaseComic
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comic(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val imageExtension: String?,
    val description: String?,
    val authors: List<String>?,
    val detailUrl: String?
) : Parcelable
