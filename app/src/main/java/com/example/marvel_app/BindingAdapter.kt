package com.example.marvel_app

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.marvel_app.model.Comic

@BindingAdapter("comics")
fun bindRecyclerView(recyclerView: RecyclerView, comics: List<Comic>?) {
    val adapter = recyclerView.adapter as ComicsAdapter
    adapter.submitList(comics)
}

@BindingAdapter("authors")
fun formatAuthors(textView: TextView, authors: List<String>?) {
    if (authors != null) {
        if (authors.isNotEmpty()) {
            var formattedText = textView.resources.getString(R.string.written_by)
            for (author in authors) {
                formattedText += " $author,"
            }
            formattedText = formattedText.subSequence(0, formattedText.length - 1).toString()
            textView.text = formattedText
        }
    }
}

@BindingAdapter("image_url", "image_resolution", "image_extension", requireAll = false)
fun bindImage(imgView: ImageView, imageUrl: String?, imageResolution: Int?, imageExtension: String?) {
    imageUrl?.let {
        val validUrl = when (imageResolution){
            0 -> "${imageUrl}/portrait_medium.$imageExtension"
            else -> "${imageUrl}.$imageExtension"
        }
        val imgUri = validUrl.toUri().buildUpon().scheme("http").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    imgView.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    imgView.visibility = View.VISIBLE
                    return false
                }
            })
            .into(imgView)
    } ?: run {
        imgView.visibility = View.GONE
    }
}