package com.example.marvel_app.network

import android.text.Html
import com.example.marvel_app.model.Comic


data class NetworkComic(
    val id: Int,
    val title: String,
    val description: String?,
    val urls: List<NetworkComicUrl?>?,
    val thumbnail: NetworkThumbnail?,
    val creators: NetworkCreators
)

fun List<NetworkComic>.asDomainModel(): List<Comic> {
    return this.map { itComic ->
        var imageUrl: String? = null
        itComic.thumbnail?.let {
            imageUrl = "${it.path}/portrait_medium.${it.extension}"
        }
        val authors: MutableList<String> = mutableListOf()
        if (itComic.creators.items.isNotEmpty()) {
            for (creator in itComic.creators.items) {
                if (creator.role == "writer") {
                    authors.add(creator.name)
                }
            }
        }
        var description: String? = null
        itComic.description?.let {
            description = Html.fromHtml(itComic.description, Html.FROM_HTML_MODE_LEGACY).toString()
        }
        Comic(
            title = itComic.title,
            imageUrl = itComic.thumbnail?.path,
            imageExtension = itComic.thumbnail?.extension,
            description = description,
            authors = authors
        )
    }
}
