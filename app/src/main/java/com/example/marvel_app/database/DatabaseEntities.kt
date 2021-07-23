package com.example.marvel_app.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.marvel_app.model.Comic
import com.example.marvel_app.network.NetworkComic

@Entity
data class DatabaseComic(
    @PrimaryKey val id: Int,
    val title: String,
    val imageUrl: String?,
    val imageExtension: String?,
    val description: String?,
    val authors: List<String>?,
    val detailUrl: String?
)

fun List<DatabaseComic>.asDomainModel(): List<Comic> {
    return map {
        Comic(
            id = it.id,
            title = it.title,
            imageUrl = it.imageUrl,
            imageExtension = it.imageExtension,
            description = it.description,
            authors = it.authors,
            detailUrl = it.detailUrl
        )
    }
}
