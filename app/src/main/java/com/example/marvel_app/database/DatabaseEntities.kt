package com.example.marvel_app.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.marvel_app.model.Comic

@Entity
data class DatabaseComic(
    @PrimaryKey val id: Int,
    val title: String,
    val imageUrl: String?,
    val imageExtension: String?,
    val description: String?,
    val detailUrl: String?)

@Entity
data class Author(
    @PrimaryKey val name: String,
    val comicId: Int
)

data class DatabaseComicWithAuthors(
    @Embedded
    val databaseComic: DatabaseComic,
    @Relation(parentColumn = "id", entityColumn = "comicId")
    val authors: List<Author>
)


fun List<DatabaseComicWithAuthors>.asDomainModel(): List<Comic> {
    return map { databaseComicWithAuthors ->
        val authors = databaseComicWithAuthors.authors.map {
            it.name
        }
        Comic(
            id = databaseComicWithAuthors.databaseComic.id,
            title = databaseComicWithAuthors.databaseComic.title,
            imageUrl = databaseComicWithAuthors.databaseComic.imageUrl,
            imageExtension = databaseComicWithAuthors.databaseComic.imageExtension,
            description = databaseComicWithAuthors.databaseComic.description,
            authors = authors,
            detailUrl = databaseComicWithAuthors.databaseComic.detailUrl
        )
    }
}
